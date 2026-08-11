package airlineReservation.domain.user.service;

import airlineReservation.domain.user.serviceInput.LoginServiceInput;
import airlineReservation.domain.user.serviceOutput.LoginServiceOutput;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.UnauthorizedException;
import airlineReservation.global.security.JwtTokenProvider;
import airlineReservation.infra.entity.User;
import airlineReservation.infra.entity.UserExample;
import airlineReservation.infra.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会員ログイン処理を行うサービス。
 */
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * メールアドレスとパスワードで認証し、アクセストークンを発行する。
     *
     * @param input
     * @return serviceOutput
     * @throws InvalidInputValueException 入力項目が誤っている場合
     * @throws UnauthorizedException 認証に失敗した場合
     */
    public LoginServiceOutput login(LoginServiceInput input) {
        validateInput(input);

        UserExample example = new UserExample();
        example.createCriteria()
                .andEmailEqualTo(input.getEmail())
                .andIsDeletedEqualTo(false);

        // メールアドレス間違いや退会した会員の場合、例外発生
        List<User> users = userMapper.selectByExample(example);
        if (users.isEmpty()) {
            throw new UnauthorizedException(ErrorCode.LOGIN_FAILED);
        }

        // パスワードが一致しない場合、例外発生
        User user = users.get(0);
        if (!passwordEncoder.matches(input.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(ErrorCode.LOGIN_FAILED);
        }

        // Accessトークン発行
        String accessToken = jwtTokenProvider.createToken(
                user.getEmail(),
                user.getUserId(),
                user.getRoleCode(),
                user.getUserName()
        );

        // レスポンス
        return LoginServiceOutput.builder()
                .success(true)
                .accessToken(accessToken)
                .userName(user.getUserName())
                .build();
    }

    /**
     * 入力値チェックを行う。
     *
     * @param input
     * @throws InvalidInputValueException
     */
    private void validateInput(LoginServiceInput input) {
        if (input.getEmail() == null || input.getEmail().isBlank()) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "メールアドレスを入力してください。");
        }
        if (input.getPassword() == null || input.getPassword().isBlank()) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "パスワードを入力してください。");
        }
    }
}
