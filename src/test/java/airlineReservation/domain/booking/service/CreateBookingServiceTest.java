package airlineReservation.domain.booking.service;

import airlineReservation.domain.booking.serviceInput.CreateBookingServiceInput;
import airlineReservation.domain.booking.serviceOutput.CreateBookingServiceOutput;
import airlineReservation.global.constant.Const;
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
@DisplayName("항공권 예약 생성 서비스")
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
                .name("홍길동");
    }

    @Test
    @DisplayName("회원 ID가 없으면 예외가 발생한다")
    void create_failsWhenUserIdIsNull() {
        CreateBookingServiceInput input = CreateBookingServiceInput.builder()
                .scheduleId(1)
                .totalPrice(100000)
                .passengerList(List.of(passenger))
                .build();

        assertThatThrownBy(() -> createBookingService.create(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원 ID를 입력해 주세요.");
    }

    @Test
    @DisplayName("운항 일정 ID가 없으면 예외가 발생한다")
    void create_failsWhenScheduleIdIsNull() {
        CreateBookingServiceInput input = CreateBookingServiceInput.builder()
                .userId(1)
                .totalPrice(100000)
                .passengerList(List.of(passenger))
                .build();

        assertThatThrownBy(() -> createBookingService.create(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("운항 일정 ID를 입력해 주세요.");
    }

    @Test
    @DisplayName("결제 금액이 0 이하면 예외가 발생한다")
    void create_failsWhenTotalPriceIsInvalid() {
        CreateBookingServiceInput input = CreateBookingServiceInput.builder()
                .userId(1)
                .scheduleId(1)
                .totalPrice(0)
                .passengerList(List.of(passenger))
                .build();

        assertThatThrownBy(() -> createBookingService.create(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("결제 금액을 확인해 주세요.");
    }

    @Test
    @DisplayName("탑승객 정보가 없으면 예외가 발생한다")
    void create_failsWhenPassengerListIsEmpty() {
        CreateBookingServiceInput input = CreateBookingServiceInput.builder()
                .userId(1)
                .scheduleId(1)
                .totalPrice(100000)
                .passengerList(List.of())
                .build();

        assertThatThrownBy(() -> createBookingService.create(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("탑승객 정보를 입력해 주세요.");
    }

    @Test
    @DisplayName("좌석이 선택되지 않으면 예외가 발생한다")
    void create_failsWhenSeatIsBlank() {
        CreateBookingRequestPassengerListInner noSeatPassenger = new CreateBookingRequestPassengerListInner()
                .name("홍길동");

        CreateBookingServiceInput input = CreateBookingServiceInput.builder()
                .userId(1)
                .scheduleId(1)
                .totalPrice(100000)
                .passengerList(List.of(noSeatPassenger))
                .build();

        assertThatThrownBy(() -> createBookingService.create(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("좌석을 선택해 주세요.");
    }

    @Test
    @DisplayName("탑승객 이름이 없으면 예외가 발생한다")
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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("탑승객 이름을 입력해 주세요.");
    }

    @Test
    @DisplayName("존재하지 않는 좌석이면 예외가 발생한다")
    void create_failsWhenSeatDoesNotExist() {
        CreateBookingServiceInput input = validInput();

        when(scheduleSeatMapper.selectByExample(any(ScheduleSeatExample.class)))
                .thenReturn(List.of());

        assertThatThrownBy(() -> createBookingService.create(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 좌석입니다: 1A");
    }

    @Test
    @DisplayName("이미 예약된 좌석이면 예외가 발생한다")
    void create_failsWhenSeatIsAlreadyOccupied() {
        CreateBookingServiceInput input = validInput();

        ScheduleSeat occupiedSeat = availableSeat();
        occupiedSeat.setStatus(Const.BOOKING_STATUS.OCCUPIED);

        when(scheduleSeatMapper.selectByExample(any(ScheduleSeatExample.class)))
                .thenReturn(List.of(occupiedSeat));

        assertThatThrownBy(() -> createBookingService.create(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("선택한 좌석이 이미 예약되었습니다: 1A");
    }

    @Test
    @DisplayName("유효한 입력으로 예약을 생성하면 bookingId를 반환한다")
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
    @DisplayName("복수 탑승객 예약 시 탑승객 수만큼 좌석과 상세 정보를 저장한다")
    void create_succeedsWithMultiplePassengers() {
        CreateBookingRequestPassengerListInner secondPassenger = new CreateBookingRequestPassengerListInner()
                .seat("1B")
                .name("김철수");

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
