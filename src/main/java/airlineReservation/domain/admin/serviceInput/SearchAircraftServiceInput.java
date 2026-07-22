package airlineReservation.domain.admin.serviceInput;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchAircraftServiceInput {

    private final String aircraftId;
    private final String aircraftName;

}
