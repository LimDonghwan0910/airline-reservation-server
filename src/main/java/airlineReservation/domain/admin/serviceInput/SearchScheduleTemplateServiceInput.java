package airlineReservation.domain.admin.serviceInput;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchScheduleTemplateServiceInput {

    private final String aircraftId;

}
