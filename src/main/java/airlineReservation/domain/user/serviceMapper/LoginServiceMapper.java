package airlineReservation.domain.user.serviceMapper;

import airlineReservation.domain.user.serviceInput.LoginServiceInput;
import airlineReservation.domain.user.serviceOutput.LoginServiceOutput;
import airlineReservation.infra.dto.LoginRequest;
import airlineReservation.infra.dto.LoginResponse;
import org.springframework.stereotype.Component;

@Component
public class LoginServiceMapper {

    public LoginServiceInput toServiceInput(LoginRequest request) {
        if (request == null) {
            return null;
        }

        return LoginServiceInput.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    public LoginResponse toResponse(LoginServiceOutput output) {
        LoginResponse response = new LoginResponse();
        response.setSuccess(output.getSuccess());
        response.setAccessToken(output.getAccessToken());
        response.setUserName(output.getUserName());
        return response;
    }
}
