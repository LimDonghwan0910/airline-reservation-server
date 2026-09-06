package airlineReservation.domain.booking.service;

import airlineReservation.domain.booking.serviceInput.CreateBookingServiceInput;
import airlineReservation.domain.booking.serviceInput.DeleteBookingServiceInput;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.DuplicateException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.dto.CreateBookingRequestPassengerListInner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@DisplayName("予約・キャンセルの同時実行制御")
class BookingConcurrencyIntegrationTest {

    private static final int CONCURRENT_REQUESTS = 24;
    private static final int SCHEDULE_ID = 101;

    @Autowired
    private CreateBookingService createBookingService;

    @Autowired
    private DeleteBookingService deleteBookingService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanUp() {
        jdbcTemplate.update("DELETE FROM passenger_details");
        jdbcTemplate.update("DELETE FROM bookings");
        jdbcTemplate.update("DELETE FROM schedule_seats");
    }

    @Test
    @DisplayName("同じ座席を同時に予約した場合、正確に1件だけ成功し座席はOCCUPIED状態になる")
    void create_allowsExactlyOneBookingForTheSameSeatUnderContention() throws Exception {
        jdbcTemplate.update("""
                INSERT INTO schedule_seats (schedule_id, seat_name, status, created_by, created_at, updated_by, updated_at)
                VALUES (?, '1A', 'AVAILABLE', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP)
                """, SCHEDULE_ID);

        List<Throwable> results = runConcurrently(() -> {
            try {
                createBookingService.create(CreateBookingServiceInput.builder()
                        .userId(1)
                        .scheduleId(SCHEDULE_ID)
                        .totalPrice(100_000)
                        .passengerList(List.of(new CreateBookingRequestPassengerListInner().seat("1A").name("tester")))
                        .build());
                return null;
            } catch (Throwable throwable) {
                return throwable;
            }
        });

        assertThat(results).hasSize(CONCURRENT_REQUESTS);
        assertThat(results.stream().filter(result -> result == null)).hasSize(1);
        assertThat(results.stream().filter(DuplicateException.class::isInstance)).hasSize(CONCURRENT_REQUESTS - 1);
        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM bookings", Integer.class)).isEqualTo(1);
        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM passenger_details", Integer.class)).isEqualTo(1);
        assertThat(jdbcTemplate.queryForObject(
                "SELECT status FROM schedule_seats WHERE schedule_id = ? AND seat_name = '1A'",
                String.class,
                SCHEDULE_ID
        )).isEqualTo(Const.SEAT_STATUS.OCCUPIED);
    }

    @Test
    @DisplayName("同じ予約を同時にキャンセルした場合、正確に1件だけ成功し座席はAVAILABLE状態に復元される")
    void cancel_allowsExactlyOneCancellationForTheSameBookingUnderContention() throws Exception {
        jdbcTemplate.update("""
                INSERT INTO schedule_seats (schedule_id, seat_name, status, created_by, created_at, updated_by, updated_at)
                VALUES (?, '1A', 'OCCUPIED', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP)
                """, SCHEDULE_ID);
        Integer seatId = jdbcTemplate.queryForObject(
                "SELECT scheduled_seat_no FROM schedule_seats WHERE schedule_id = ? AND seat_name = '1A'",
                Integer.class,
                SCHEDULE_ID
        );
        jdbcTemplate.update("""
                INSERT INTO bookings (user_id, schedule_id, total_price, booking_time, status, is_deleted, created_by, created_at, updated_by, updated_at)
                VALUES (1, ?, 100000, CURRENT_TIMESTAMP, 'COMPLETED', FALSE, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP)
                """, SCHEDULE_ID);
        Integer bookingId = jdbcTemplate.queryForObject("SELECT booking_id FROM bookings", Integer.class);
        jdbcTemplate.update("""
                INSERT INTO passenger_details (booking_id, passenger_name, schedule_seat_no, is_deleted, created_by, created_at, updated_by, updated_at)
                VALUES (?, 'tester', ?, FALSE, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP)
                """, bookingId, seatId);

        List<Throwable> results = runConcurrently(() -> {
            try {
                deleteBookingService.delete(DeleteBookingServiceInput.builder()
                        .bookingId(bookingId)
                        .updatedBy(2)
                        .build());
                return null;
            } catch (Throwable throwable) {
                return throwable;
            }
        });

        assertThat(results).hasSize(CONCURRENT_REQUESTS);
        assertThat(results.stream().filter(result -> result == null)).hasSize(1);
        assertThat(results.stream().filter(NotFoundException.class::isInstance)).hasSize(CONCURRENT_REQUESTS - 1);
        assertThat(jdbcTemplate.queryForObject("SELECT status FROM bookings WHERE booking_id = ?", String.class, bookingId))
                .isEqualTo(Const.BOOKING_STATUS.CANCELLED);
        assertThat(jdbcTemplate.queryForObject("SELECT is_deleted FROM bookings WHERE booking_id = ?", Boolean.class, bookingId))
                .isTrue();
        assertThat(jdbcTemplate.queryForObject("SELECT status FROM schedule_seats WHERE scheduled_seat_no = ?", String.class, seatId))
                .isEqualTo(Const.SEAT_STATUS.AVAILABLE);
        assertThat(jdbcTemplate.queryForObject("SELECT is_deleted FROM passenger_details WHERE booking_id = ?", Boolean.class, bookingId))
                .isTrue();
    }

    private List<Throwable> runConcurrently(Callable<Throwable> action) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_REQUESTS);
        CountDownLatch ready = new CountDownLatch(CONCURRENT_REQUESTS);
        CountDownLatch start = new CountDownLatch(1);
        try {
            List<Future<Throwable>> futures = new ArrayList<>();
            for (int i = 0; i < CONCURRENT_REQUESTS; i++) {
                futures.add(executor.submit(() -> {
                    ready.countDown();
                    assertThat(start.await(10, TimeUnit.SECONDS)).isTrue();
                    return action.call();
                }));
            }
            assertThat(ready.await(10, TimeUnit.SECONDS)).isTrue();
            start.countDown();

            List<Throwable> results = new ArrayList<>();
            for (Future<Throwable> future : futures) {
                results.add(future.get(15, TimeUnit.SECONDS));
            }
            return results;
        } finally {
            executor.shutdownNow();
            assertThat(executor.awaitTermination(10, TimeUnit.SECONDS)).isTrue();
        }
    }
}
