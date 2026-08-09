package airlineReservation.domain.user.service;

import airlineReservation.domain.booking.service.DeleteBookingService;
import airlineReservation.domain.booking.serviceInput.DeleteBookingServiceInput;
import airlineReservation.domain.user.serviceInput.DeleteAccountServiceInput;
import airlineReservation.domain.user.serviceOutput.DeleteAccountServiceOutput;
import airlineReservation.global.constant.Const;
import airlineReservation.infra.entity.Booking;
import airlineReservation.infra.entity.BookingExample;
import airlineReservation.infra.entity.User;
import airlineReservation.infra.mapper.BookingMapper;
import airlineReservation.infra.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteAccountService {

    private final UserMapper userMapper;
    private final BookingMapper bookingMapper;
    private final DeleteBookingService deleteBookingService;

    @Transactional
    public DeleteAccountServiceOutput delete(DeleteAccountServiceInput input) {
        if (input.getUserId() == null) {
            throw new IllegalArgumentException("회원 ID를 입력해 주세요.");
        }

        User user = userMapper.selectByPrimaryKey(input.getUserId());
        if (user == null || Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        cancelActiveBookings(input.getUserId());

        LocalDateTime now = LocalDateTime.now();
        User update = new User();
        update.setUserId(input.getUserId());
        update.setIsDeleted(true);
        update.setUpdatedBy(input.getUserId());
        update.setUpdatedAt(now);
        userMapper.updateByPrimaryKeySelective(update);

        return DeleteAccountServiceOutput.builder().build();
    }

    private void cancelActiveBookings(Integer userId) {
        BookingExample example = new BookingExample();
        example.createCriteria()
                .andUserIdEqualTo(userId)
                .andIsDeletedEqualTo(false);

        List<Booking> bookings = bookingMapper.selectByExample(example);
        for (Booking booking : bookings) {
            if (Const.BOOKING_STATUS.CANCELLED.equals(booking.getStatus())) {
                continue;
            }

            deleteBookingService.delete(DeleteBookingServiceInput.builder()
                    .bookingId(booking.getBookingId())
                    .updatedBy(userId)
                    .build());
        }
    }
}
