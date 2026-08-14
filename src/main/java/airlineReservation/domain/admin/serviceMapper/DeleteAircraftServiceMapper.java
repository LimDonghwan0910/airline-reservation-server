package airlineReservation.domain.admin.serviceMapper;

import airlineReservation.domain.admin.serviceInput.DeleteAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.DeleteAircraftServiceOutput;
import airlineReservation.infra.dto.DeleteAircraftRequest;
import airlineReservation.infra.dto.DeleteAircraftResponse;
import org.springframework.stereotype.Component;

@Component
public class DeleteAircraftServiceMapper {

    public DeleteAircraftServiceInput toServiceInput(DeleteAircraftRequest request) {
        if (request == null) {
            return null;
        }

        return DeleteAircraftServiceInput.builder()
                .aircraftId(request.getAircraftId())
                .build();
    }

    public DeleteAircraftResponse toResponse(DeleteAircraftServiceOutput output) {

        return new DeleteAircraftResponse();
    }

}
