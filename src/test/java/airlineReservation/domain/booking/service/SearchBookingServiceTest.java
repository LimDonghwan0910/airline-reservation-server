package airlineReservation.domain.booking.service;

import airlineReservation.domain.admin.serviceInput.SearchBookingByAdminServiceInput;
import airlineReservation.domain.booking.serviceInput.SearchBookingServiceInput;
import airlineReservation.domain.booking.serviceOutput.SearchBookingServiceOutput;
import airlineReservation.domain.booking.vo.SearchBookingVo;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.InvalidInputValueException;
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
@DisplayName("航空券予約検索サービス")
class SearchBookingServiceTest {

    @Mock
    private SearchBookingCustomMapper searchBookingCustomMapper;

    @InjectMocks
    private SearchBookingService searchBookingService;

    @Test
    @DisplayName("会員IDがない場合は例外が発生する")
    void searchByMember_failsWhenUserIdIsNull() {
        SearchBookingServiceInput input = SearchBookingServiceInput.builder()
                .fromDate(LocalDate.of(2026, 7, 1))
                .build();

        assertThatThrownBy(() -> searchBookingService.searchByMember(input))
                .isInstanceOf(InvalidInputValueException.class)
                .hasMessage("会員IDを入力してください。");
    }

    @Test
    @DisplayName("検索結果がない場合は空の一覧を返す")
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
    @DisplayName("同一予約の flat row を bookingId 単位でグループ化する")
    void searchByMember_groupsFlatRowsByBookingId() {
        SearchBookingServiceInput input = SearchBookingServiceInput.builder()
                .userId(1)
                .build();

        LocalDateTime departure = LocalDateTime.of(2026, 8, 1, 9, 0);
        LocalDateTime arrival = LocalDateTime.of(2026, 8, 1, 11, 0);
        LocalDateTime bookedTime = LocalDateTime.of(2026, 7, 20, 14, 30);

        List<SearchBookingVo> flatRows = List.of(
                bookingRow(1, "山田太郎", "1A", departure, arrival, bookedTime),
                bookingRow(1, "佐藤花子", "1B", departure, arrival, bookedTime),
                bookingRow(2, "鈴木一郎", "2A", departure.plusDays(1), arrival.plusDays(1), bookedTime.plusDays(1))
        );

        when(searchBookingCustomMapper.selectBookingListForMember(1, null))
                .thenReturn(flatRows);

        SearchBookingServiceOutput output = searchBookingService.searchByMember(input);

        assertThat(output.getBookingList()).hasSize(2);

        SearchBookingServiceOutput.BookingItem firstBooking = output.getBookingList().get(0);
        assertThat(firstBooking.getBookingId()).isEqualTo(1);
        assertThat(firstBooking.getUserName()).isEqualTo("テストユーザー");
        assertThat(firstBooking.getPassengerCount()).isEqualTo(2);
        assertThat(firstBooking.getSeats()).extracting("seat")
                .containsExactly("1A", "1B");
        assertThat(firstBooking.getSeats()).extracting("name")
                .containsExactly("山田太郎", "佐藤花子");

        SearchBookingServiceOutput.BookingItem secondBooking = output.getBookingList().get(1);
        assertThat(secondBooking.getBookingId()).isEqualTo(2);
        assertThat(secondBooking.getPassengerCount()).isEqualTo(1);
        assertThat(secondBooking.getSeats()).extracting("seat")
                .containsExactly("2A");
    }

    @Test
    @DisplayName("fromDate 条件で会員予約を検索する")
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
    @DisplayName("管理者検索時、フィルタ条件を mapper に渡す")
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
                .userName("テストユーザー")
                .aircraftId("B737")
                .departureAirportId("GMP")
                .arrivalAirportId("CJU")
                .departureDatetime(departure)
                .arrivalDatetime(arrival)
                .status(Const.BOOKING_STATUS.COMPLETED)
                .totalPrice(150000)
                .bookedTime(bookedTime)
                .passengerName(passengerName)
                .seatName(seatName)
                .build();
    }
}
