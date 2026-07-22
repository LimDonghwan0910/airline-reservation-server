package airlineReservation.domain.admin.serviceInput;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteAircraftServiceInput {

    private String aircraftId;

}
