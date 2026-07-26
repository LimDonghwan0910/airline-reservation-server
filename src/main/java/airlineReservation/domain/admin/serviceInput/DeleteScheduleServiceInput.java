package airlineReservation.domain.admin.serviceInput;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteScheduleServiceInput {

    private final Integer scheduleId;

}
