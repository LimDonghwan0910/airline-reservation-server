package airlineReservation.domain.booking.service;

import airlineReservation.domain.booking.serviceInput.DeleteBookingServiceInput;
import airlineReservation.domain.booking.serviceOutput.DeleteBookingServiceOutput;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.entity.Booking;
import airlineReservation.infra.entity.PassengerDetail;
import airlineReservation.infra.entity.PassengerDetailExample;
import airlineReservation.infra.entity.ScheduleSeat;
import airlineReservation.infra.mapper.BookingMapper;
import airlineReservation.infra.mapper.PassengerDetailMapper;
import airlineReservation.infra.mapper.ScheduleSeatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteBookingService {

    private final BookingMapper bookingMapper;
    private final PassengerDetailMapper passengerDetailMapper;
    private final ScheduleSeatMapper scheduleSeatMapper;

    @Transactional
    public DeleteBookingServiceOutput delete(DeleteBookingServiceInput input) {
        if (input.getBookingId() == null) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "予約IDを入力してください。");
        }

        Booking booking = bookingMapper.selectByPrimaryKey(input.getBookingId());
        if (booking == null || Boolean.TRUE.equals(booking.getIsDeleted())) {
            throw new NotFoundException(ErrorCode.BOOKING_NOT_FOUND);
        }
        if (Const.BOOKING_STATUS.CANCELLED.equals(booking.getStatus())) {
            throw new InvalidInputValueException(ErrorCode.BOOKING_ALREADY_CANCELLED);
        }

        releaseSeatsForBooking(input.getBookingId(), input.getUpdatedBy());

        LocalDateTime now = LocalDateTime.now();
        Booking update = new Booking();
        update.setBookingId(input.getBookingId());
        update.setStatus(Const.BOOKING_STATUS.CANCELLED);
        update.setIsDeleted(true);
        update.setUpdatedAt(now);
        if (input.getUpdatedBy() != null) {
            update.setUpdatedBy(input.getUpdatedBy());
        }
        bookingMapper.updateByPrimaryKeySelective(update);

        return DeleteBookingServiceOutput.builder().build();
    }

    private void releaseSeatsForBooking(Integer bookingId, Integer updatedBy) {
        PassengerDetailExample example = new PassengerDetailExample();
        example.createCriteria()
                .andBookingIdEqualTo(bookingId)
                .andIsDeletedEqualTo(false);

        List<PassengerDetail> passengers = passengerDetailMapper.selectByExample(example);
        LocalDateTime now = LocalDateTime.now();

        for (PassengerDetail passenger : passengers) {
            ScheduleSeat seatUpdate = new ScheduleSeat();
            seatUpdate.setScheduledSeatNo(passenger.getScheduleSeatNo());
            seatUpdate.setStatus(Const.SEAT_STATUS.AVAILABLE);
            seatUpdate.setUpdatedAt(now);
            if (updatedBy != null) {
                seatUpdate.setUpdatedBy(updatedBy);
            }
            scheduleSeatMapper.updateByPrimaryKeySelective(seatUpdate);

            PassengerDetail passengerUpdate = new PassengerDetail();
            passengerUpdate.setPassengerDetailId(passenger.getPassengerDetailId());
            passengerUpdate.setIsDeleted(true);
            passengerUpdate.setUpdatedAt(now);
            if (updatedBy != null) {
                passengerUpdate.setUpdatedBy(updatedBy);
            }
            passengerDetailMapper.updateByPrimaryKeySelective(passengerUpdate);
        }
    }
}
