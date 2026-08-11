package airlineReservation.domain.user.service;

import airlineReservation.domain.user.serviceInput.CreateAccountServiceInput;
import airlineReservation.domain.user.serviceOutput.CreateAccountServiceOutput;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.DuplicateException;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.infra.entity.User;
import airlineReservation.infra.entity.UserExample;
import airlineReservation.infra.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * CreateAccountService の単体テスト。
 * UserMapper / PasswordEncoder をモックし、入力検証・重複チェック・登録処理を検証する。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("会員登録サービス")
class CreateAccountServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateAccountService createAccountService;

    @Nested
    @DisplayName("正常系")
    class Success {

        @Test
        @DisplayName("有効な入力で会員登録すると成功する")
        void create_succeedsWithValidInput() {
            // Given: 有効な入力、メール重複なし、採番後の userId を返す
            CreateAccountServiceInput input = validInput();

            when(userMapper.selectByExample(any(UserExample.class)))
                    .thenReturn(List.of());
            when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
            // insert 時に DB 採番をシミュレートする
            when(userMapper.insertSelective(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setUserId(10);
                return 1;
            });

            // When: 会員登録を実行する
            CreateAccountServiceOutput output = createAccountService.create(input);

            // Then: 登録内容と監査項目（created_by / updated_by）が正しく設定される
            assertThat(output).isNotNull();

            ArgumentCaptor<User> insertCaptor = ArgumentCaptor.forClass(User.class);
            verify(userMapper).insertSelective(insertCaptor.capture());

            User inserted = insertCaptor.getValue();
            assertThat(inserted.getRoleCode()).isEqualTo(Const.USER_ROLE.MEMBER);
            assertThat(inserted.getUserName()).isEqualTo("山田太郎");
            assertThat(inserted.getEmail()).isEqualTo("taro@example.com");
            assertThat(inserted.getPhoneNumber()).isEqualTo("09012345678");
            assertThat(inserted.getBirthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
            assertThat(inserted.getPassword()).isEqualTo("encodedPassword");
            assertThat(inserted.getIsDeleted()).isFalse();
            assertThat(inserted.getCreatedAt()).isNotNull();
            assertThat(inserted.getUpdatedAt()).isNotNull();

            ArgumentCaptor<User> auditCaptor = ArgumentCaptor.forClass(User.class);
            verify(userMapper).updateByPrimaryKeySelective(auditCaptor.capture());

            User audit = auditCaptor.getValue();
            assertThat(audit.getUserId()).isEqualTo(10);
            assertThat(audit.getCreatedBy()).isEqualTo(10);
            assertThat(audit.getUpdatedBy()).isEqualTo(10);
        }
    }

    @Nested
    @DisplayName("例外系")
    class Failure {

        @Test
        @DisplayName("名前がない場合は例外が発生する")
        void create_failsWhenUserNameIsBlank() {
            // Given: 名前が空白のみの入力
            CreateAccountServiceInput input = CreateAccountServiceInput.builder()
                    .userName(" ")
                    .email("taro@example.com")
                    .password("password123")
                    .phoneNumber("09012345678")
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .build();

            // When: 会員登録を実行する
            // Then: 入力値例外が発生し、DB登録は行われない
            assertThatThrownBy(() -> createAccountService.create(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("名前を入力してください。");
            verify(userMapper, never()).insertSelective(any());
        }

        @Test
        @DisplayName("メールアドレスがない場合は例外が発生する")
        void create_failsWhenEmailIsBlank() {
            // Given: メールアドレスが空文字の入力
            CreateAccountServiceInput input = CreateAccountServiceInput.builder()
                    .userName("山田太郎")
                    .email("")
                    .password("password123")
                    .phoneNumber("09012345678")
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .build();

            // When: 会員登録を実行する
            // Then: 入力値例外が発生し、DB登録は行われない
            assertThatThrownBy(() -> createAccountService.create(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("メールアドレスを入力してください。");
            verify(userMapper, never()).insertSelective(any());
        }

        @Test
        @DisplayName("パスワードがない場合は例外が発生する")
        void create_failsWhenPasswordIsBlank() {
            // Given: パスワードが null の入力
            CreateAccountServiceInput input = CreateAccountServiceInput.builder()
                    .userName("山田太郎")
                    .email("taro@example.com")
                    .password(null)
                    .phoneNumber("09012345678")
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .build();

            // When: 会員登録を実行する
            // Then: 入力値例外が発生し、DB登録は行われない
            assertThatThrownBy(() -> createAccountService.create(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("パスワードを入力してください。");
            verify(userMapper, never()).insertSelective(any());
        }

        @Test
        @DisplayName("電話番号がない場合は例外が発生する")
        void create_failsWhenPhoneNumberIsBlank() {
            // Given: 電話番号が空白のみの入力
            CreateAccountServiceInput input = CreateAccountServiceInput.builder()
                    .userName("山田太郎")
                    .email("taro@example.com")
                    .password("password123")
                    .phoneNumber("  ")
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .build();

            // When: 会員登録を実行する
            // Then: 入力値例外が発生し、DB登録は行われない
            assertThatThrownBy(() -> createAccountService.create(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("電話番号を入力してください。");
            verify(userMapper, never()).insertSelective(any());
        }

        @Test
        @DisplayName("生年月日がない場合は例外が発生する")
        void create_failsWhenBirthDateIsNull() {
            // Given: 生年月日が null の入力
            CreateAccountServiceInput input = CreateAccountServiceInput.builder()
                    .userName("山田太郎")
                    .email("taro@example.com")
                    .password("password123")
                    .phoneNumber("09012345678")
                    .birthDate(null)
                    .build();

            // When: 会員登録を実行する
            // Then: 入力値例外が発生し、DB登録は行われない
            assertThatThrownBy(() -> createAccountService.create(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("生年月日を入力してください。");
            verify(userMapper, never()).insertSelective(any());
        }

        @Test
        @DisplayName("既に使用されているメールアドレスの場合は例外が発生する")
        void create_failsWhenEmailIsDuplicated() {
            // Given: 同一メールアドレスの未退会会員が既に存在する
            CreateAccountServiceInput input = validInput();

            User existingUser = new User();
            existingUser.setUserId(1);
            existingUser.setEmail(input.getEmail());

            when(userMapper.selectByExample(any(UserExample.class)))
                    .thenReturn(List.of(existingUser));

            // When: 会員登録を実行する
            // Then: 重複例外が発生し、登録・パスワード暗号化は行われない
            assertThatThrownBy(() -> createAccountService.create(input))
                    .isInstanceOfSatisfying(DuplicateException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo(ErrorCode.DUPLICATE_EMAIL.getMessage());
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_EMAIL);
                    });
            verify(userMapper, never()).insertSelective(any());
            verify(passwordEncoder, never()).encode(anyString());
        }
    }

    /** 正常系で共通利用する有効な入力データを返す。 */
    private CreateAccountServiceInput validInput() {
        return CreateAccountServiceInput.builder()
                .userName("山田太郎")
                .email("taro@example.com")
                .password("password123")
                .phoneNumber("09012345678")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();
    }
}
