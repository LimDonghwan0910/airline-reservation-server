package airlineReservation.domain.admin.serviceMapper;

import airlineReservation.domain.admin.serviceInput.CreateAircraftServiceInput;
import airlineReservation.domain.admin.serviceInput.DeleteAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.CreateAircraftServiceOutput;
import airlineReservation.domain.admin.serviceOutput.DeleteAircraftServiceOutput;
import airlineReservation.infra.dto.CreateAircraftRequest;
import airlineReservation.infra.dto.CreateAircraftResponse;
import airlineReservation.infra.dto.DeleteAircraftRequest;
import airlineReservation.infra.dto.DeleteAircraftResponse;
import org.springframework.stereotype.Component;

@Component
public class DeleteAircraftServiceMapper {

    public DeleteAircraftServiceInput toServiceInput(DeleteAircraftRequest request) {
        if (request == null) {
            return null;
        }

        // ② 빌더 패턴(Builder Pattern)이나 기본 생성자+Setter를 사용해 값을 복사합니다.
        return DeleteAircraftServiceInput.builder()
                .aircraftId(request.getAircraftId())
                .build();
    }

    /**
     * Service Output ➔ Controller Response 변환
     */
    public DeleteAircraftResponse toResponse(DeleteAircraftServiceOutput output) {

        return new DeleteAircraftResponse();
    }

}
