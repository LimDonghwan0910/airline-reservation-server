package airlineReservation.domain.booking.serviceOutput;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 예약 조회 서비스 결과.
 *
 * DB에서는 좌석마다 row가 나오지만, Service의 buildOutput()에서
 * 예약 1건 = BookingItem + seats[] 형태로 묶인다.
 */
@Getter
@Builder
public class SearchBookingServiceOutput {

    private final List<BookingItem> bookingList;

    /** 예약 1건 (항공편 정보 + 탑승객/좌석 목록) */
    @Getter
    @Builder
    public static class BookingItem {

        private final Integer bookingId;
        private final Integer userId;
        private final Integer scheduleId;
        private final String userName;
        private final String aircraftId;
        private final String departureAirportId;
        private final String arrivalAirportId;
        private final LocalDateTime departureDatetime;
        private final LocalDateTime arrivalDatetime;
        private final String status;
        private final Integer totalPrice;
        /** 탑승객 수 = seats.size() */
        private final Integer passengerCount;
        private final List<SeatItem> seats;
        private final LocalDateTime bookedTime;

    }

    /** 탑승객 1명의 좌석 정보 */
    @Getter
    @Builder
    public static class SeatItem {

        private final String seat;
        private final String name;

    }

}
