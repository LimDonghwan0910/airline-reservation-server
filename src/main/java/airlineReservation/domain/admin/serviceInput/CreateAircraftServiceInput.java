package airlineReservation.domain.admin.serviceInput;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class CreateAircraftServiceInput {

    private String aircraftId;

    private String aircraftName;

    private Integer rowCount;

    private Integer columnCount;

}
