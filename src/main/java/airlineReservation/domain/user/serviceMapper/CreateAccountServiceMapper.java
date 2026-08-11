package airlineReservation.domain.user.serviceMapper;

import airlineReservation.domain.user.serviceInput.CreateAccountServiceInput;
import airlineReservation.domain.user.serviceOutput.CreateAccountServiceOutput;
import airlineReservation.infra.dto.CreateAccountRequest;
import airlineReservation.infra.dto.CreateAccountResponse;
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
