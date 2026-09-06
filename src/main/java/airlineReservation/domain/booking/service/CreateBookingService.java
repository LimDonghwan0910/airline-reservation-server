package airlineReservation.domain.booking.service;

import airlineReservation.domain.booking.serviceInput.CreateBookingServiceInput;
import airlineReservation.domain.booking.serviceOutput.CreateBookingServiceOutput;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.DuplicateException;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.dto.CreateBookingRequestPassengerListInner;
import airlineReservation.infra.entity.Booking;
import airlineReservation.infra.entity.PassengerDetail;
import airlineReservation.infra.entity.ScheduleSeat;
import airlineReservation.infra.mapper.BookingMapper;
import airlineReservation.infra.mapper.PassengerDetailMapper;
import airlineReservation.infra.mapper.ScheduleSeatMapper;
import airlineReservation.infra.mapper.customMapper.BookingCustomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreateBookingService {

    private final BookingMapper bookingMapper;
    private final PassengerDetailMapper passengerDetailMapper;
    private final ScheduleSeatMapper scheduleSeatMapper;
    private final BookingCustomMapper bookingCustomMapper;

    /**
     * 予約を作成する。選択座席を行ロックしてから空席判定と占有更新を同一トランザクションで行う。
     *
     * @throws InvalidInputValueException 入力項目が誤っている場合
     * @throws NotFoundException 指定座席が存在しない場合
     * @throws DuplicateException 指定座席が重複している、または既に予約済みの場合
     */
    @Transactional
    public CreateBookingServiceOutput create(CreateBookingServiceInput input) {
        validateInput(input);

        List<ScheduleSeat> reservedSeats = resolveAvailableSeats(input.getScheduleId(), input.getPassengerList());
        LocalDateTime now = LocalDateTime.now();

        Booking booking = new Booking();
        booking.setUserId(input.getUserId());
        booking.setScheduleId(input.getScheduleId());
        booking.setTotalPrice(input.getTotalPrice());
        booking.setBookingTime(now);
        booking.setStatus(Const.BOOKING_STATUS.COMPLETED);
        booking.setIsDeleted(false);
        booking.setCreatedBy(input.getUserId());
        booking.setCreatedAt(now);
        booking.setUpdatedBy(input.getUserId());
        booking.setUpdatedAt(now);
        bookingMapper.insertSelective(booking);

        for (int i = 0; i < input.getPassengerList().size(); i++) {
            CreateBookingRequestPassengerListInner passenger = input.getPassengerList().get(i);
            ScheduleSeat scheduleSeat = reservedSeats.get(i);

            PassengerDetail passengerDetail = new PassengerDetail();
            passengerDetail.setBookingId(booking.getBookingId());
            passengerDetail.setPassengerName(passenger.getName());
            passengerDetail.setScheduleSeatNo(scheduleSeat.getScheduledSeatNo());
            passengerDetail.setIsDeleted(false);
            passengerDetail.setCreatedBy(input.getUserId());
            passengerDetail.setCreatedAt(now);
            passengerDetail.setUpdatedBy(input.getUserId());
            passengerDetail.setUpdatedAt(now);
            passengerDetailMapper.insertSelective(passengerDetail);

            ScheduleSeat seatUpdate = new ScheduleSeat();
            seatUpdate.setScheduledSeatNo(scheduleSeat.getScheduledSeatNo());
            seatUpdate.setStatus(Const.SEAT_STATUS.OCCUPIED);
            seatUpdate.setUpdatedBy(input.getUserId());
            seatUpdate.setUpdatedAt(now);
            scheduleSeatMapper.updateByPrimaryKeySelective(seatUpdate);
        }

        return CreateBookingServiceOutput.builder()
                .bookingId(booking.getBookingId())
                .build();
    }

    private void validateInput(CreateBookingServiceInput input) {
        if (input.getUserId() == null) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "会員IDを入力してください。");
        }
        if (input.getScheduleId() == null) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "運航スケジュールIDを入力してください。");
        }
        if (input.getTotalPrice() == null || input.getTotalPrice() <= 0) {
            throw new InvalidInputValueException(ErrorCode.INVALID_INPUT_VALUE, "支払金額を確認してください。");
        }
        if (input.getPassengerList() == null || input.getPassengerList().isEmpty()) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "搭乗者情報を入力してください。");
        }

        Set<String> selectedSeats = new HashSet<>();
        for (CreateBookingRequestPassengerListInner passenger : input.getPassengerList()) {
            if (passenger.getSeat() == null || passenger.getSeat().isBlank()) {
                throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "座席を選択してください。");
            }
            if (!selectedSeats.add(passenger.getSeat())) {
                throw new DuplicateException(
                        ErrorCode.DUPLICATE_SEAT,
                        "同じ座席を複数の搭乗者に指定できません: " + passenger.getSeat()
                );
            }
            if (passenger.getName() == null || passenger.getName().isBlank()) {
                throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "搭乗者名を入力してください。");
            }
        }
    }

    private List<ScheduleSeat> resolveAvailableSeats(
            Integer scheduleId,
            List<CreateBookingRequestPassengerListInner> passengerList
    ) {
        List<String> seatNames = passengerList.stream()
                .map(CreateBookingRequestPassengerListInner::getSeat)
                .toList();
        List<ScheduleSeat> lockedSeats = bookingCustomMapper.selectScheduleSeatsForUpdate(scheduleId, seatNames);
        Map<String, ScheduleSeat> lockedSeatByName = new HashMap<>();
        for (ScheduleSeat lockedSeat : lockedSeats) {
            lockedSeatByName.put(lockedSeat.getSeatName(), lockedSeat);
        }

        List<ScheduleSeat> reservedSeats = new ArrayList<>();
        for (CreateBookingRequestPassengerListInner passenger : passengerList) {
            ScheduleSeat scheduleSeat = lockedSeatByName.get(passenger.getSeat());
            if (scheduleSeat == null) {
                throw new NotFoundException(ErrorCode.SEAT_NOT_FOUND, "存在しない座席です: " + passenger.getSeat());
            }

            if (!Const.SEAT_STATUS.AVAILABLE.equals(scheduleSeat.getStatus())) {
                throw new DuplicateException(
                        ErrorCode.DUPLICATE_SEAT,
                        "選択した座席は既に予約されています: " + passenger.getSeat()
                );
            }

            reservedSeats.add(scheduleSeat);
        }

        return reservedSeats;
    }
}
