package airlineReservation.domain.user.service;

import airlineReservation.domain.user.serviceInput.LoginServiceInput;
import airlineReservation.domain.user.serviceOutput.LoginServiceOutput;
import airlineReservation.global.security.JwtTokenProvider;
import airlineReservation.infra.entity.User;
import airlineReservation.infra.entity.UserExample;
import airlineReservation.infra.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginServiceOutput login(LoginServiceInput input) {
        validateInput(input);

        UserExample example = new UserExample();
        example.createCriteria()
                .andEmailEqualTo(input.getEmail())
                .andIsDeletedEqualTo(false);

        List<User> users = userMapper.selectByExample(example);
        if (users.isEmpty()) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        User user = users.get(0);
        if (!passwordEncoder.matches(input.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createToken(
                user.getEmail(),
                user.getUserId(),
                user.getRoleCode()
        );

        return LoginServiceOutput.builder()
                .success(true)
                .accessToken(accessToken)
                .build();
    }

    private void validateInput(LoginServiceInput input) {
        if (input.getEmail() == null || input.getEmail().isBlank()) {
            throw new IllegalArgumentException("이메일을 입력해 주세요.");
        }
        if (input.getPassword() == null || input.getPassword().isBlank()) {
            throw new IllegalArgumentException("비밀번호를 입력해 주세요.");
        }
    }
}
