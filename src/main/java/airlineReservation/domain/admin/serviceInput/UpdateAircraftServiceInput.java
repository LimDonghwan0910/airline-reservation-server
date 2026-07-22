package airlineReservation.domain.admin.serviceInput;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateAircraftServiceInput {

    private String aircraftId;

    private String aircraftName;

    private Integer rowCount;

    private Integer columnCount;

}
