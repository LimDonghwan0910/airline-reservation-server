package airlineReservation.domain.booking.service;

import airlineReservation.domain.booking.serviceInput.DeleteBookingServiceInput;
import airlineReservation.domain.booking.serviceOutput.DeleteBookingServiceOutput;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.entity.Booking;
import airlineReservation.infra.entity.PassengerDetail;
import airlineReservation.infra.entity.PassengerDetailExample;
import airlineReservation.infra.entity.ScheduleSeat;
import airlineReservation.infra.mapper.BookingMapper;
import airlineReservation.infra.mapper.PassengerDetailMapper;
import airlineReservation.infra.mapper.ScheduleSeatMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("航空券予約キャンセルサービス")
class DeleteBookingServiceTest {

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private PassengerDetailMapper passengerDetailMapper;

    @Mock
    private ScheduleSeatMapper scheduleSeatMapper;

    @InjectMocks
    private DeleteBookingService deleteBookingService;

    @Test
    @DisplayName("予約IDがない場合は例外が発生する")
    void delete_failsWhenBookingIdIsNull() {
        DeleteBookingServiceInput input = DeleteBookingServiceInput.builder()
                .updatedBy(1)
                .build();

        assertThatThrownBy(() -> deleteBookingService.delete(input))
                .isInstanceOf(InvalidInputValueException.class)
                .hasMessage("予約IDを入力してください。");
    }

    @Test
    @DisplayName("存在しない予約の場合は例外が発生する")
    void delete_failsWhenBookingNotFound() {
        DeleteBookingServiceInput input = DeleteBookingServiceInput.builder()
                .bookingId(999)
                .updatedBy(1)
                .build();

        when(bookingMapper.selectByPrimaryKey(999)).thenReturn(null);

        assertThatThrownBy(() -> deleteBookingService.delete(input))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("予約が見つかりません。");
    }

    @Test
    @DisplayName("既に削除された予約の場合は例外が発生する")
    void delete_failsWhenBookingIsDeleted() {
        DeleteBookingServiceInput input = DeleteBookingServiceInput.builder()
                .bookingId(1)
                .updatedBy(1)
                .build();

        Booking deletedBooking = activeBooking();
        deletedBooking.setIsDeleted(true);

        when(bookingMapper.selectByPrimaryKey(1)).thenReturn(deletedBooking);

        assertThatThrownBy(() -> deleteBookingService.delete(input))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("予約が見つかりません。");
    }

    @Test
    @DisplayName("既にキャンセルされた予約の場合は例外が発生する")
    void delete_failsWhenBookingIsAlreadyCancelled() {
        DeleteBookingServiceInput input = DeleteBookingServiceInput.builder()
                .bookingId(1)
                .updatedBy(1)
                .build();

        Booking cancelledBooking = activeBooking();
        cancelledBooking.setStatus(Const.BOOKING_STATUS.CANCELLED);

        when(bookingMapper.selectByPrimaryKey(1)).thenReturn(cancelledBooking);

        assertThatThrownBy(() -> deleteBookingService.delete(input))
                .isInstanceOf(InvalidInputValueException.class)
                .hasMessage("既にキャンセルされた予約です。");
    }

    @Test
    @DisplayName("有効な予約をキャンセルすると座席を AVAILABLE に戻す")
    void delete_succeedsAndReleasesSeats() {
        DeleteBookingServiceInput input = DeleteBookingServiceInput.builder()
                .bookingId(1)
                .updatedBy(10)
                .build();

        when(bookingMapper.selectByPrimaryKey(1)).thenReturn(activeBooking());

        PassengerDetail passenger = new PassengerDetail();
        passenger.setPassengerDetailId(100);
        passenger.setScheduleSeatNo(50);

        when(passengerDetailMapper.selectByExample(any(PassengerDetailExample.class)))
                .thenReturn(List.of(passenger));

        DeleteBookingServiceOutput output = deleteBookingService.delete(input);

        assertThat(output).isNotNull();

        ArgumentCaptor<ScheduleSeat> seatCaptor = ArgumentCaptor.forClass(ScheduleSeat.class);
        verify(scheduleSeatMapper).updateByPrimaryKeySelective(seatCaptor.capture());
        assertThat(seatCaptor.getValue().getStatus()).isEqualTo(Const.BOOKING_STATUS.AVAILABLE);
        assertThat(seatCaptor.getValue().getScheduledSeatNo()).isEqualTo(50);

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingMapper).updateByPrimaryKeySelective(bookingCaptor.capture());
        assertThat(bookingCaptor.getValue().getStatus()).isEqualTo(Const.BOOKING_STATUS.CANCELLED);
        assertThat(bookingCaptor.getValue().getIsDeleted()).isTrue();
    }

    @Test
    @DisplayName("搭乗者がいない予約でもキャンセル処理は完了する")
    void delete_succeedsWhenNoPassengers() {
        DeleteBookingServiceInput input = DeleteBookingServiceInput.builder()
                .bookingId(1)
                .updatedBy(10)
                .build();

        when(bookingMapper.selectByPrimaryKey(1)).thenReturn(activeBooking());
        when(passengerDetailMapper.selectByExample(any(PassengerDetailExample.class)))
                .thenReturn(List.of());

        DeleteBookingServiceOutput output = deleteBookingService.delete(input);

        assertThat(output).isNotNull();
        verify(scheduleSeatMapper, never()).updateByPrimaryKeySelective(any());
        verify(bookingMapper).updateByPrimaryKeySelective(any());
    }

    private Booking activeBooking() {
        Booking booking = new Booking();
        booking.setBookingId(1);
        booking.setUserId(5);
        booking.setScheduleId(10);
        booking.setStatus(Const.BOOKING_STATUS.COMPLETED);
        booking.setIsDeleted(false);
        return booking;
    }
}
