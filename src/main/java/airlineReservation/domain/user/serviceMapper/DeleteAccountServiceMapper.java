package airlineReservation.domain.user.serviceMapper;

import airlineReservation.domain.user.serviceInput.DeleteAccountServiceInput;
import airlineReservation.domain.user.serviceOutput.DeleteAccountServiceOutput;
import airlineReservation.infra.dto.DeleteAccountRequest;
import airlineReservation.infra.dto.DeleteAccountResponse;
import org.springframework.stereotype.Component;

@Component
public class DeleteAccountServiceMapper {

    public DeleteAccountServiceInput toServiceInput(DeleteAccountRequest request) {
        if (request == null || request.getUserId() == null || request.getUserId().isBlank()) {
            throw new IllegalArgumentException("회원 ID를 입력해 주세요.");
        }

        try {
            return DeleteAccountServiceInput.builder()
                    .userId(Integer.parseInt(request.getUserId()))
                    .build();
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("회원 ID 형식이 올바르지 않습니다.");
        }
    }

    public DeleteAccountResponse toResponse(DeleteAccountServiceOutput output) {
        DeleteAccountResponse response = new DeleteAccountResponse();
        response.setSuccess(true);
        return response;
    }
}
