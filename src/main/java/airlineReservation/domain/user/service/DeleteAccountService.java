package airlineReservation.domain.user.service;

import airlineReservation.domain.user.serviceInput.DeleteAccountServiceInput;
import airlineReservation.domain.user.serviceOutput.DeleteAccountServiceOutput;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.ConflictException;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.entity.BookingExample;
import airlineReservation.infra.entity.User;
import airlineReservation.infra.mapper.BookingMapper;
import airlineReservation.infra.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 会員退会処理を行うサービス。
 */
@Service
@RequiredArgsConstructor
public class DeleteAccountService {

    private final UserMapper userMapper;
    private final BookingMapper bookingMapper;

    /**
     * 会員を退会（論理削除）する。
     * メールアドレスはUNIQUE制約回避のためマスキングする。
     *
     * @param input
     * @return serviceOutput
     * @throws InvalidInputValueException 入力項目が誤っている場合
     * @throws NotFoundException 対象会員が存在しない、または既に退会済みの場合
     * @throws ConflictException 有効な予約が存在する場合
     */
    @Transactional
    public DeleteAccountServiceOutput delete(DeleteAccountServiceInput input) {
        if (input.getUserId() == null) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "会員IDを入力してください。");
        }

        User user = userMapper.selectByPrimaryKey(input.getUserId());
        if (user == null || Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        // 有効予約存在チェック
        ensureNoActiveBookings(input.getUserId());

        LocalDateTime now = LocalDateTime.now();

        // UNIQUE制約回避のためメールアドレスをマスキング
        String timeStamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String maskedEmail = "deleted_" + timeStamp + "_" + user.getEmail();

        User update = new User();
        update.setUserId(input.getUserId());
        update.setEmail(maskedEmail);
        update.setIsDeleted(true);
        update.setUpdatedBy(input.getUserId());
        update.setUpdatedAt(now);
        userMapper.updateByPrimaryKeySelective(update);

        return DeleteAccountServiceOutput.builder().build();
    }

    /**
     * 有効な予約が残っていないことを確認する。
     *
     * @param userId チェック対象の会員ID
     * @throws ConflictException 有効な予約が存在する場合
     */
    private void ensureNoActiveBookings(Integer userId) {
        BookingExample example = new BookingExample();
        example.createCriteria()
                .andUserIdEqualTo(userId)
                .andIsDeletedEqualTo(false)
                .andStatusNotEqualTo(Const.BOOKING_STATUS.CANCELLED);

        if (bookingMapper.countByExample(example) > 0) {
            throw new ConflictException(ErrorCode.ACTIVE_BOOKING_EXISTS);
        }
    }
}
