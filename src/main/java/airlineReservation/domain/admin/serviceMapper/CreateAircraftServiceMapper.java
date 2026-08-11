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

        // ② Builder Pattern または基本コンストラクタ + Setter で値をコピーする
        return CreateAircraftServiceInput.builder()
                .aircraftId(request.getAircraftId())
                .aircraftName(request.getAircraftName())
                .rowCount(request.getRowCount())
                .columnCount(request.getColumnCount())
                .build();
    }

    /**
     * Service Output → Controller Response 変換
     */
    public CreateAircraftResponse toResponse(CreateAircraftServiceOutput output) {

        return new CreateAircraftResponse();
    }

}
