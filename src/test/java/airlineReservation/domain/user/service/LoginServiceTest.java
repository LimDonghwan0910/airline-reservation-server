package airlineReservation.domain.user.service;

import airlineReservation.domain.user.serviceInput.LoginServiceInput;
import airlineReservation.domain.user.serviceOutput.LoginServiceOutput;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.UnauthorizedException;
import airlineReservation.global.security.JwtTokenProvider;
import airlineReservation.infra.entity.User;
import airlineReservation.infra.entity.UserExample;
import airlineReservation.infra.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * LoginService の単体テスト。
 * UserMapper / PasswordEncoder / JwtTokenProvider をモックし、
 * 入力検証・認証失敗・トークン発行を検証する。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("会員ログインサービス")
class LoginServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private LoginService loginService;

    @Test
    @DisplayName("メールアドレスがない場合は例外が発生する")
    void login_failsWhenEmailIsBlank() {
        // Given: メールアドレスが空白のみの入力
        LoginServiceInput input = LoginServiceInput.builder()
                .email(" ")
                .password("password123")
                .build();

        // When: ログインを実行する
        Throwable thrown = catchThrowable(() -> loginService.login(input));

        // Then: 入力値例外が発生し、認証・トークン発行は行われない
        assertThat(thrown)
                .isInstanceOf(InvalidInputValueException.class)
                .hasMessage("メールアドレスを入力してください。");
        verify(userMapper, never()).selectByExample(any());
        verify(jwtTokenProvider, never()).createToken(any(), any(), any(), any());
    }

    @Test
    @DisplayName("パスワードがない場合は例外が発生する")
    void login_failsWhenPasswordIsBlank() {
        // Given: パスワードが空文字の入力
        LoginServiceInput input = LoginServiceInput.builder()
                .email("taro@example.com")
                .password("")
                .build();

        // When: ログインを実行する
        Throwable thrown = catchThrowable(() -> loginService.login(input));

        // Then: 入力値例外が発生し、認証・トークン発行は行われない
        assertThat(thrown)
                .isInstanceOf(InvalidInputValueException.class)
                .hasMessage("パスワードを入力してください。");
        verify(userMapper, never()).selectByExample(any());
        verify(jwtTokenProvider, never()).createToken(any(), any(), any(), any());
    }

    @Test
    @DisplayName("存在しないメールアドレスの場合は例外が発生する")
    void login_failsWhenUserNotFound() {
        // Given: 該当する未退会会員が存在しない
        LoginServiceInput input = validInput();

        when(userMapper.selectByExample(any(UserExample.class)))
                .thenReturn(List.of());

        // When: ログインを実行する
        Throwable thrown = catchThrowable(() -> loginService.login(input));

        // Then: 認証失敗例外が発生し、パスワード照合・トークン発行は行われない
        assertThat(thrown)
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorCode.LOGIN_FAILED.getMessage())
                .extracting(ex -> ((UnauthorizedException) ex).getErrorCode())
                .isEqualTo(ErrorCode.LOGIN_FAILED);
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtTokenProvider, never()).createToken(any(), any(), any(), any());
    }

    @Test
    @DisplayName("パスワードが一致しない場合は例外が発生する")
    void login_failsWhenPasswordDoesNotMatch() {
        // Given: 会員は存在するがパスワードが一致しない
        LoginServiceInput input = validInput();
        User user = existingUser();

        when(userMapper.selectByExample(any(UserExample.class)))
                .thenReturn(List.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword"))
                .thenReturn(false);

        // When: ログインを実行する
        Throwable thrown = catchThrowable(() -> loginService.login(input));

        // Then: 認証失敗例外が発生し、トークンは発行されない
        assertThat(thrown)
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorCode.LOGIN_FAILED.getMessage())
                .extracting(ex -> ((UnauthorizedException) ex).getErrorCode())
                .isEqualTo(ErrorCode.LOGIN_FAILED);
        verify(jwtTokenProvider, never()).createToken(any(), any(), any(), any());
    }

    @Test
    @DisplayName("有効な認証情報でログインするとアクセストークンを返す")
    void login_succeedsWithValidCredentials() {
        // Given: 会員が存在し、パスワード照合・トークン発行が成功する
        LoginServiceInput input = validInput();
        User user = existingUser();

        when(userMapper.selectByExample(any(UserExample.class)))
                .thenReturn(List.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword"))
                .thenReturn(true);
        when(jwtTokenProvider.createToken(
                "taro@example.com",
                10,
                Const.USER_ROLE.MEMBER,
                "山田太郎"
        )).thenReturn("jwt-access-token");

        // When: ログインを実行する
        LoginServiceOutput output = loginService.login(input);

        // Then: 成功レスポンスとアクセストークンが返却される
        assertThat(output.getSuccess()).isTrue();
        assertThat(output.getAccessToken()).isEqualTo("jwt-access-token");
        assertThat(output.getUserName()).isEqualTo("山田太郎");
        verify(jwtTokenProvider).createToken(
                "taro@example.com",
                10,
                Const.USER_ROLE.MEMBER,
                "山田太郎"
        );
    }

    /** 正常系で共通利用する有効なログイン入力を返す。 */
    private LoginServiceInput validInput() {
        return LoginServiceInput.builder()
                .email("taro@example.com")
                .password("password123")
                .build();
    }

    /** 認証対象となる未退会会員のテストデータを返す。 */
    private User existingUser() {
        User user = new User();
        user.setUserId(10);
        user.setEmail("taro@example.com");
        user.setPassword("encodedPassword");
        user.setUserName("山田太郎");
        user.setRoleCode(Const.USER_ROLE.MEMBER);
        user.setIsDeleted(false);
        return user;
    }
}
