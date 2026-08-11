package airlineReservation.domain.user.serviceMapper;

import airlineReservation.domain.user.serviceInput.DeleteAccountServiceInput;
import airlineReservation.domain.user.serviceOutput.DeleteAccountServiceOutput;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.infra.dto.DeleteAccountRequest;
import airlineReservation.infra.dto.DeleteAccountResponse;
import org.springframework.stereotype.Component;

@Component
public class DeleteAccountServiceMapper {

    public DeleteAccountServiceInput toServiceInput(DeleteAccountRequest request) {
        if (request == null || request.getUserId() == null || request.getUserId().isBlank()) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "会員IDを入力してください。");
        }

        try {
            return DeleteAccountServiceInput.builder()
                    .userId(Integer.parseInt(request.getUserId()))
                    .build();
        } catch (NumberFormatException ex) {
            throw new InvalidInputValueException(ErrorCode.INVALID_INPUT_VALUE, "会員IDの形式が正しくありません。");
        }
    }

    public DeleteAccountResponse toResponse(DeleteAccountServiceOutput output) {
        DeleteAccountResponse response = new DeleteAccountResponse();
        response.setSuccess(true);
        return response;
    }
}
