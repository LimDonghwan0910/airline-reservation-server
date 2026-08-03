package airlineReservation.domain.admin.serviceMapper;

import airlineReservation.domain.admin.serviceInput.CreateAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.CreateAircraftServiceOutput;
import airlineReservation.infra.dto.CreateAircraftRequest;
import airlineReservation.infra.dto.CreateAircraftResponse;
import org.springframework.stereotype.Component;

@Component
public class CreateAircraftServiceMapper {

    public CreateAircraftServiceInput toServiceInput(CreateAircraftRequest request) {
        if (request == null) {
            return null;
        }

        // ② 빌더 패턴(Builder Pattern)이나 기본 생성자+Setter를 사용해 값을 복사합니다.
        return CreateAircraftServiceInput.builder()
                .aircraftId(request.getAircraftId())
                .aircraftName(request.getAircraftName())
                .rowCount(request.getRowCount())
                .columnCount(request.getColumnCount())
                .build();
    }

    /**
     * Service Output ➔ Controller Response 변환
     */
    public CreateAircraftResponse toResponse(CreateAircraftServiceOutput output) {

        return new CreateAircraftResponse();
    }

}
