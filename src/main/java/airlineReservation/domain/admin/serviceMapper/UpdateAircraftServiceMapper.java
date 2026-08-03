package airlineReservation.domain.admin.serviceMapper;

import airlineReservation.domain.admin.serviceInput.CreateAircraftServiceInput;
import airlineReservation.domain.admin.serviceInput.UpdateAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.CreateAircraftServiceOutput;
import airlineReservation.domain.admin.serviceOutput.UpdateAircraftServiceOutput;
import airlineReservation.infra.dto.CreateAircraftRequest;
import airlineReservation.infra.dto.CreateAircraftResponse;
import airlineReservation.infra.dto.UpdateAircraftRequest;
import airlineReservation.infra.dto.UpdateAircraftResponse;
import org.springframework.stereotype.Component;

@Component
public class UpdateAircraftServiceMapper {

    public UpdateAircraftServiceInput toServiceInput(UpdateAircraftRequest request) {
        if (request == null) {
            return null;
        }

        return UpdateAircraftServiceInput.builder()
                .aircraftId(request.getAircraftId())
                .aircraftName(request.getAircraftName())
                .rowCount(request.getRowCount())
                .columnCount(request.getColumnCount())
                .build();
    }

    public UpdateAircraftResponse toResponse(UpdateAircraftServiceOutput output) {

        return new UpdateAircraftResponse();
    }

}
