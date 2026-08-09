package airlineReservation.domain.user.serviceMapper;

import airlineReservation.domain.user.serviceInput.CreateAccountServiceInput;
import airlineReservation.domain.user.serviceInput.DeleteAccountServiceInput;
import airlineReservation.domain.user.serviceInput.LoginServiceInput;
import airlineReservation.domain.user.serviceOutput.CreateAccountServiceOutput;
import airlineReservation.domain.user.serviceOutput.DeleteAccountServiceOutput;
import airlineReservation.domain.user.serviceOutput.LoginServiceOutput;
import airlineReservation.infra.dto.CreateAccountRequest;
import airlineReservation.infra.dto.CreateAccountResponse;
import airlineReservation.infra.dto.DeleteAccountRequest;
import airlineReservation.infra.dto.DeleteAccountResponse;
import airlineReservation.infra.dto.LoginRequest;
import airlineReservation.infra.dto.LoginResponse;
import org.springframework.stereotype.Component;

@Component
public class CreateAccountServiceMapper {

    public CreateAccountServiceInput toServiceInput(CreateAccountRequest request) {
        if (request == null) {
            return null;
        }

        return CreateAccountServiceInput.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .birthDate(request.getBirthDate())
                .build();
    }

    public CreateAccountResponse toResponse(CreateAccountServiceOutput output) {
        CreateAccountResponse response = new CreateAccountResponse();
        response.setSuccess(true);
        return response;
    }
}
