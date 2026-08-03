package airlineReservation.domain.common.serviceInput;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetSeatServiceInput {

    private final Integer scheduleId;

}
