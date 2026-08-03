package airlineReservation.domain.booking.service;

import airlineReservation.domain.admin.serviceInput.SearchBookingByAdminServiceInput;
import airlineReservation.domain.booking.serviceInput.SearchBookingServiceInput;
import airlineReservation.domain.booking.serviceOutput.SearchBookingServiceOutput;
import airlineReservation.domain.booking.vo.SearchBookingVo;
import airlineReservation.infra.mapper.customMapper.SearchBookingCustomMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("항공권 예약 조회 서비스")
class SearchBookingServiceTest {

    @Mock
    private SearchBookingCustomMapper searchBookingCustomMapper;

    @InjectMocks
    private SearchBookingService searchBookingService;

    @Test
    @DisplayName("회원 ID가 없으면 예외가 발생한다")
    void searchByMember_failsWhenUserIdIsNull() {
        SearchBookingServiceInput input = SearchBookingServiceInput.builder()
                .fromDate(LocalDate.of(2026, 7, 1))
                .build();

        assertThatThrownBy(() -> searchBookingService.searchByMember(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원 ID를 입력해 주세요.");
    }

    @Test
    @DisplayName("조회 결과가 없으면 빈 목록을 반환한다")
    void searchByMember_returnsEmptyListWhenNoBookings() {
        SearchBookingServiceInput input = SearchBookingServiceInput.builder()
                .userId(1)
                .build();

        when(searchBookingCustomMapper.selectBookingListForMember(1, null))
                .thenReturn(List.of());

        SearchBookingServiceOutput output = searchBookingService.searchByMember(input);

        assertThat(output.getBookingList()).isEmpty();
    }

    @Test
    @DisplayName("동일 예약의 flat row를 bookingId 기준으로 그룹화한다")
    void searchByMember_groupsFlatRowsByBookingId() {
        SearchBookingServiceInput input = SearchBookingServiceInput.builder()
                .userId(1)
                .build();

        LocalDateTime departure = LocalDateTime.of(2026, 8, 1, 9, 0);
        LocalDateTime arrival = LocalDateTime.of(2026, 8, 1, 11, 0);
        LocalDateTime bookedTime = LocalDateTime.of(2026, 7, 20, 14, 30);

        List<SearchBookingVo> flatRows = List.of(
                bookingRow(1, "홍길동", "1A", departure, arrival, bookedTime),
                bookingRow(1, "김철수", "1B", departure, arrival, bookedTime),
                bookingRow(2, "이영희", "2A", departure.plusDays(1), arrival.plusDays(1), bookedTime.plusDays(1))
        );

        when(searchBookingCustomMapper.selectBookingListForMember(1, null))
                .thenReturn(flatRows);

        SearchBookingServiceOutput output = searchBookingService.searchByMember(input);

        assertThat(output.getBookingList()).hasSize(2);

        SearchBookingServiceOutput.BookingItem firstBooking = output.getBookingList().get(0);
        assertThat(firstBooking.getBookingId()).isEqualTo(1);
        assertThat(firstBooking.getUserName()).isEqualTo("테스트유저");
        assertThat(firstBooking.getPassengerCount()).isEqualTo(2);
        assertThat(firstBooking.getSeats()).extracting("seat")
                .containsExactly("1A", "1B");
        assertThat(firstBooking.getSeats()).extracting("name")
                .containsExactly("홍길동", "김철수");

        SearchBookingServiceOutput.BookingItem secondBooking = output.getBookingList().get(1);
        assertThat(secondBooking.getBookingId()).isEqualTo(2);
        assertThat(secondBooking.getPassengerCount()).isEqualTo(1);
        assertThat(secondBooking.getSeats()).extracting("seat")
                .containsExactly("2A");
    }

    @Test
    @DisplayName("fromDate 조건으로 회원 예약을 조회한다")
    void searchByMember_passesFromDateToMapper() {
        LocalDate fromDate = LocalDate.of(2026, 7, 1);
        SearchBookingServiceInput input = SearchBookingServiceInput.builder()
                .userId(5)
                .fromDate(fromDate)
                .build();

        when(searchBookingCustomMapper.selectBookingListForMember(5, fromDate))
                .thenReturn(List.of());

        searchBookingService.searchByMember(input);

        verify(searchBookingCustomMapper).selectBookingListForMember(5, fromDate);
    }

    @Test
    @DisplayName("관리자 조회 시 필터 조건을 mapper에 전달한다")
    void searchByAdmin_passesFilterConditionsToMapper() {
        LocalDate departureDate = LocalDate.of(2026, 8, 1);
        LocalDate arrivalDate = LocalDate.of(2026, 8, 2);

        SearchBookingByAdminServiceInput input = SearchBookingByAdminServiceInput.builder()
                .userId(3)
                .aircraftId("B737")
                .departureDate(departureDate)
                .arrivalDate(arrivalDate)
                .build();

        when(searchBookingCustomMapper.selectBookingListForAdmin(3, "B737", departureDate, arrivalDate))
                .thenReturn(List.of());

        SearchBookingServiceOutput output = searchBookingService.searchByAdmin(input);

        assertThat(output.getBookingList()).isEmpty();
        verify(searchBookingCustomMapper).selectBookingListForAdmin(3, "B737", departureDate, arrivalDate);
    }

    private SearchBookingVo bookingRow(
            int bookingId,
            String passengerName,
            String seatName,
            LocalDateTime departure,
            LocalDateTime arrival,
            LocalDateTime bookedTime
    ) {
        return SearchBookingVo.builder()
                .bookingId(bookingId)
                .userId(1)
                .scheduleId(100 + bookingId)
                .userName("테스트유저")
                .aircraftId("B737")
                .departureAirportId("GMP")
                .arrivalAirportId("CJU")
                .departureDatetime(departure)
                .arrivalDatetime(arrival)
                .status("COMPLETED")
                .totalPrice(150000)
                .bookedTime(bookedTime)
                .passengerName(passengerName)
                .seatName(seatName)
                .build();
    }
}
