package airlineReservation.domain.user.serviceMapper;

import airlineReservation.domain.user.serviceOutput.DeleteAccountServiceOutput;
import airlineReservation.infra.dto.DeleteAccountResponse;
import org.springframework.stereotype.Component;

@Component
public class DeleteAccountServiceMapper {

    public DeleteAccountResponse toResponse(DeleteAccountServiceOutput output) {
        DeleteAccountResponse response = new DeleteAccountResponse();
        response.setSuccess(true);
        return response;
    }
}
