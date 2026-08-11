package airlineReservation.domain.user.service;

import airlineReservation.domain.user.serviceInput.DeleteAccountServiceInput;
import airlineReservation.domain.user.serviceOutput.DeleteAccountServiceOutput;
import airlineReservation.global.exception.ConflictException;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.entity.BookingExample;
import airlineReservation.infra.entity.User;
import airlineReservation.infra.mapper.BookingMapper;
import airlineReservation.infra.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * DeleteAccountService の単体テスト。
 * UserMapper / BookingMapper をモックし、
 * 入力検証・会員存在確認・有効予約チェック・論理削除を検証する。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("会員退会サービス")
class DeleteAccountServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private DeleteAccountService deleteAccountService;

    @Test
    @DisplayName("会員IDがない場合は例外が発生する")
    void delete_failsWhenUserIdIsNull() {
        // Given: 会員IDが null の入力
        DeleteAccountServiceInput input = DeleteAccountServiceInput.builder()
                .userId(null)
                .build();

        // When: 退会処理を実行する
        Throwable thrown = catchThrowable(() -> deleteAccountService.delete(input));

        // Then: 入力値例外が発生し、照会・更新は行われない
        assertThat(thrown)
                .isInstanceOf(InvalidInputValueException.class)
                .hasMessage("会員IDを入力してください。");
        verify(userMapper, never()).selectByPrimaryKey(any());
        verify(userMapper, never()).updateByPrimaryKeySelective(any());
    }

    @Test
    @DisplayName("存在しない会員の場合は例外が発生する")
    void delete_failsWhenUserDoesNotExist() {
        // Given: 指定IDの会員が存在しない
        DeleteAccountServiceInput input = validInput();

        when(userMapper.selectByPrimaryKey(10)).thenReturn(null);

        // When: 退会処理を実行する
        Throwable thrown = catchThrowable(() -> deleteAccountService.delete(input));

        // Then: NotFound 例外が発生し、予約確認・更新は行われない
        assertThat(thrown)
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.MEMBER_NOT_FOUND.getMessage())
                .extracting(ex -> ((NotFoundException) ex).getErrorCode())
                .isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
        verify(bookingMapper, never()).countByExample(any());
        verify(userMapper, never()).updateByPrimaryKeySelective(any());
    }

    @Test
    @DisplayName("既に退会済みの会員の場合は例外が発生する")
    void delete_failsWhenUserIsAlreadyDeleted() {
        // Given: 既に論理削除済みの会員
        DeleteAccountServiceInput input = validInput();

        User deletedUser = existingUser();
        deletedUser.setIsDeleted(true);

        when(userMapper.selectByPrimaryKey(10)).thenReturn(deletedUser);

        // When: 退会処理を実行する
        Throwable thrown = catchThrowable(() -> deleteAccountService.delete(input));

        // Then: NotFound 例外が発生し、予約確認・更新は行われない
        assertThat(thrown)
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.MEMBER_NOT_FOUND.getMessage())
                .extracting(ex -> ((NotFoundException) ex).getErrorCode())
                .isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
        verify(bookingMapper, never()).countByExample(any());
        verify(userMapper, never()).updateByPrimaryKeySelective(any());
    }

    @Test
    @DisplayName("有効な予約がある場合は例外が発生する")
    void delete_failsWhenActiveBookingExists() {
        // Given: 会員は存在するが、キャンセル以外の有効予約が残っている
        DeleteAccountServiceInput input = validInput();

        when(userMapper.selectByPrimaryKey(10)).thenReturn(existingUser());
        when(bookingMapper.countByExample(any(BookingExample.class))).thenReturn(1L);

        // When: 退会処理を実行する
        Throwable thrown = catchThrowable(() -> deleteAccountService.delete(input));

        // Then: 競合例外が発生し、退会更新は行われない
        assertThat(thrown)
                .isInstanceOf(ConflictException.class)
                .hasMessage(ErrorCode.ACTIVE_BOOKING_EXISTS.getMessage())
                .extracting(ex -> ((ConflictException) ex).getErrorCode())
                .isEqualTo(ErrorCode.ACTIVE_BOOKING_EXISTS);
        verify(userMapper, never()).updateByPrimaryKeySelective(any());
    }

    @Test
    @DisplayName("有効な入力で退会すると論理削除される")
    void delete_succeedsWithValidInput() {
        // Given: 未退会会員が存在し、有効予約もない
        DeleteAccountServiceInput input = validInput();
        User user = existingUser();

        when(userMapper.selectByPrimaryKey(10)).thenReturn(user);
        when(bookingMapper.countByExample(any(BookingExample.class))).thenReturn(0L);

        // When: 退会処理を実行する
        DeleteAccountServiceOutput output = deleteAccountService.delete(input);

        // Then: 論理削除され、UNIQUE制約回避のためメールがマスキングされる
        assertThat(output).isNotNull();

        ArgumentCaptor<User> updateCaptor = ArgumentCaptor.forClass(User.class);
        verify(userMapper).updateByPrimaryKeySelective(updateCaptor.capture());

        User updated = updateCaptor.getValue();
        assertThat(updated.getUserId()).isEqualTo(10);
        assertThat(updated.getIsDeleted()).isTrue();
        assertThat(updated.getUpdatedBy()).isEqualTo(10);
        assertThat(updated.getUpdatedAt()).isNotNull();
        // deleted_{yyyyMMddHHmmss}_{元メール} 形式
        assertThat(updated.getEmail())
                .startsWith("deleted_")
                .endsWith("_taro@example.com")
                .matches("deleted_\\d{14}_taro@example\\.com");
    }

    /** 正常系で共通利用する有効な退会入力を返す。 */
    private DeleteAccountServiceInput validInput() {
        return DeleteAccountServiceInput.builder()
                .userId(10)
                .build();
    }

    /** 退会対象となる未退会会員のテストデータを返す。 */
    private User existingUser() {
        User user = new User();
        user.setUserId(10);
        user.setEmail("taro@example.com");
        user.setIsDeleted(false);
        return user;
    }
}
