package airlineReservation.domain.booking.service;

import airlineReservation.domain.booking.serviceInput.CreateBookingServiceInput;
import airlineReservation.domain.booking.serviceOutput.CreateBookingServiceOutput;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.DuplicateException;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.dto.CreateBookingRequestPassengerListInner;
import airlineReservation.infra.entity.Booking;
import airlineReservation.infra.entity.ScheduleSeat;
import airlineReservation.infra.entity.ScheduleSeatExample;
import airlineReservation.infra.mapper.BookingMapper;
import airlineReservation.infra.mapper.PassengerDetailMapper;
import airlineReservation.infra.mapper.ScheduleSeatMapper;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("航空券予約作成サービス")
class CreateBookingServiceTest {

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private PassengerDetailMapper passengerDetailMapper;

    @Mock
    private ScheduleSeatMapper scheduleSeatMapper;

    @InjectMocks
    private CreateBookingService createBookingService;

    private CreateBookingRequestPassengerListInner passenger;

    @BeforeEach
    void setUp() {
        passenger = new CreateBookingRequestPassengerListInner()
                .seat("1A")
                .name("山田太郎");
    }

    @Test
    @DisplayName("会員IDがない場合は例外が発生する")
    void create_failsWhenUserIdIsNull() {
        CreateBookingServiceInput input = CreateBookingServiceInput.builder()
                .scheduleId(1)
                .totalPrice(100000)
                .passengerList(List.of(passenger))
                .build();

        assertThatThrownBy(() -> createBookingService.create(input))
                .isInstanceOf(InvalidInputValueException.class)
                .hasMessage("会員IDを入力してください。");
    }

    @Test
    @DisplayName("運航スケジュールIDがない場合は例外が発生する")
    void create_failsWhenScheduleIdIsNull() {
        CreateBookingServiceInput input = CreateBookingServiceInput.builder()
                .userId(1)
                .totalPrice(100000)
                .passengerList(List.of(passenger))
                .build();

        assertThatThrownBy(() -> createBookingService.create(input))
                .isInstanceOf(InvalidInputValueException.class)
                .hasMessage("運航スケジュールIDを入力してください。");
    }

    @Test
    @DisplayName("支払金額が0以下の場合は例外が発生する")
    void create_failsWhenTotalPriceIsInvalid() {
        CreateBookingServiceInput input = CreateBookingServiceInput.builder()
                .userId(1)
                .scheduleId(1)
                .totalPrice(0)
                .passengerList(List.of(passenger))
                .build();

        assertThatThrownBy(() -> createBookingService.create(input))
                .isInstanceOf(InvalidInputValueException.class)
                .hasMessage("支払金額を確認してください。");
    }

    @Test
    @DisplayName("搭乗者情報がない場合は例外が発生する")
    void create_failsWhenPassengerListIsEmpty() {
        CreateBookingServiceInput input = CreateBookingServiceInput.builder()
                .userId(1)
                .scheduleId(1)
                .totalPrice(100000)
                .passengerList(List.of())
                .build();

        assertThatThrownBy(() -> createBookingService.create(input))
                .isInstanceOf(InvalidInputValueException.class)
                .hasMessage("搭乗者情報を入力してください。");
    }

    @Test
    @DisplayName("座席が選択されていない場合は例外が発生する")
    void create_failsWhenSeatIsBlank() {
        CreateBookingRequestPassengerListInner noSeatPassenger = new CreateBookingRequestPassengerListInner()
                .name("山田太郎");

        CreateBookingServiceInput input = CreateBookingServiceInput.builder()
                .userId(1)
                .scheduleId(1)
                .totalPrice(100000)
                .passengerList(List.of(noSeatPassenger))
                .build();

        assertThatThrownBy(() -> createBookingService.create(input))
                .isInstanceOf(InvalidInputValueException.class)
                .hasMessage("座席を選択してください。");
    }

    @Test
    @DisplayName("搭乗者名がない場合は例外が発生する")
    void create_failsWhenPassengerNameIsBlank() {
        CreateBookingRequestPassengerListInner noNamePassenger = new CreateBookingRequestPassengerListInner()
                .seat("1A");

        CreateBookingServiceInput input = CreateBookingServiceInput.builder()
                .userId(1)
                .scheduleId(1)
                .totalPrice(100000)
                .passengerList(List.of(noNamePassenger))
                .build();

        assertThatThrownBy(() -> createBookingService.create(input))
                .isInstanceOf(InvalidInputValueException.class)
                .hasMessage("搭乗者名を入力してください。");
    }

    @Test
    @DisplayName("存在しない座席の場合は例外が発生する")
    void create_failsWhenSeatDoesNotExist() {
        CreateBookingServiceInput input = validInput();

        when(scheduleSeatMapper.selectByExample(any(ScheduleSeatExample.class)))
                .thenReturn(List.of());

        assertThatThrownBy(() -> createBookingService.create(input))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("存在しない座席です: 1A");
    }

    @Test
    @DisplayName("既に予約済みの座席の場合は例外が発生する")
    void create_failsWhenSeatIsAlreadyOccupied() {
        CreateBookingServiceInput input = validInput();

        ScheduleSeat occupiedSeat = availableSeat();
        occupiedSeat.setStatus(Const.BOOKING_STATUS.OCCUPIED);

        when(scheduleSeatMapper.selectByExample(any(ScheduleSeatExample.class)))
                .thenReturn(List.of(occupiedSeat));

        assertThatThrownBy(() -> createBookingService.create(input))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("選択した座席は既に予約されています: 1A");
    }

    @Test
    @DisplayName("有効な入力で予約を作成すると bookingId を返す")
    void create_succeedsWithValidInput() {
        CreateBookingServiceInput input = validInput();

        when(scheduleSeatMapper.selectByExample(any(ScheduleSeatExample.class)))
                .thenReturn(List.of(availableSeat()));

        when(bookingMapper.insertSelective(any(Booking.class))).thenAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            booking.setBookingId(100);
            return 1;
        });

        CreateBookingServiceOutput output = createBookingService.create(input);

        assertThat(output.getBookingId()).isEqualTo(100);

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingMapper).insertSelective(bookingCaptor.capture());

        Booking savedBooking = bookingCaptor.getValue();
        assertThat(savedBooking.getUserId()).isEqualTo(1);
        assertThat(savedBooking.getScheduleId()).isEqualTo(10);
        assertThat(savedBooking.getTotalPrice()).isEqualTo(150000);
        assertThat(savedBooking.getStatus()).isEqualTo(Const.BOOKING_STATUS.COMPLETED);

        verify(passengerDetailMapper, times(1)).insertSelective(any());
        verify(scheduleSeatMapper, times(1)).updateByPrimaryKeySelective(any());
    }

    @Test
    @DisplayName("複数搭乗者の予約時、搭乗者数分の座席と詳細情報を保存する")
    void create_succeedsWithMultiplePassengers() {
        CreateBookingRequestPassengerListInner secondPassenger = new CreateBookingRequestPassengerListInner()
                .seat("1B")
                .name("佐藤花子");

        CreateBookingServiceInput input = CreateBookingServiceInput.builder()
                .userId(1)
                .scheduleId(10)
                .totalPrice(300000)
                .passengerList(List.of(passenger, secondPassenger))
                .build();

        ScheduleSeat seat1A = availableSeat();
        seat1A.setSeatName("1A");
        ScheduleSeat seat1B = availableSeat();
        seat1B.setScheduledSeatNo(2);
        seat1B.setSeatName("1B");

        when(scheduleSeatMapper.selectByExample(any(ScheduleSeatExample.class)))
                .thenReturn(List.of(seat1A))
                .thenReturn(List.of(seat1B));

        when(bookingMapper.insertSelective(any(Booking.class))).thenAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            booking.setBookingId(200);
            return 1;
        });

        CreateBookingServiceOutput output = createBookingService.create(input);

        assertThat(output.getBookingId()).isEqualTo(200);
        verify(passengerDetailMapper, times(2)).insertSelective(any());
        verify(scheduleSeatMapper, times(2)).updateByPrimaryKeySelective(any());
    }

    private CreateBookingServiceInput validInput() {
        return CreateBookingServiceInput.builder()
                .userId(1)
                .scheduleId(10)
                .totalPrice(150000)
                .passengerList(List.of(passenger))
                .build();
    }

    private ScheduleSeat availableSeat() {
        ScheduleSeat seat = new ScheduleSeat();
        seat.setScheduledSeatNo(1);
        seat.setScheduleId(10);
        seat.setSeatName("1A");
        seat.setStatus(Const.BOOKING_STATUS.AVAILABLE);
        return seat;
    }
}
