package airlineReservation.domain.booking.service;

import airlineReservation.domain.booking.serviceInput.DeleteBookingServiceInput;
import airlineReservation.domain.booking.serviceOutput.DeleteBookingServiceOutput;
import airlineReservation.global.constant.Const;
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
            throw new IllegalArgumentException("예약 ID를 입력해 주세요.");
        }

        Booking booking = bookingMapper.selectByPrimaryKey(input.getBookingId());
        if (booking == null || Boolean.TRUE.equals(booking.getIsDeleted())) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }
        if (Const.BOOKING_STATUS.CANCELLED.equals(booking.getStatus())) {
            throw new IllegalArgumentException("이미 취소된 예약입니다.");
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
            seatUpdate.setStatus(Const.BOOKING_STATUS.AVAILABLE);
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
