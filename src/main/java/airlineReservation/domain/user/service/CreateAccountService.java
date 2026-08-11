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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateAccountService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CreateAccountServiceOutput create(CreateAccountServiceInput input) {
        validateInput(input);
        ensureEmailAvailable(input.getEmail());

        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setRoleCode(Const.USER_ROLE.MEMBER);
        user.setUserName(input.getUserName());
        user.setBirthDate(input.getBirthDate());
        user.setEmail(input.getEmail());
        user.setPhoneNumber(input.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setIsDeleted(false);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        userMapper.insertSelective(user);

        return CreateAccountServiceOutput.builder().build();
    }

    private void validateInput(CreateAccountServiceInput input) {
        if (input.getUserName() == null || input.getUserName().isBlank()) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "名前を入力してください。");
        }
        if (input.getEmail() == null || input.getEmail().isBlank()) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "メールアドレスを入力してください。");
        }
        if (input.getPassword() == null || input.getPassword().isBlank()) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "パスワードを入力してください。");
        }
        if (input.getPhoneNumber() == null || input.getPhoneNumber().isBlank()) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "電話番号を入力してください。");
        }
        if (input.getBirthDate() == null) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "生年月日を入力してください。");
        }
    }

    private void ensureEmailAvailable(String email) {
        UserExample example = new UserExample();
        example.createCriteria()
                .andEmailEqualTo(email)
                .andIsDeletedEqualTo(false);

        List<User> existingUsers = userMapper.selectByExample(example);
        if (!existingUsers.isEmpty()) {
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL);
        }
    }
}
