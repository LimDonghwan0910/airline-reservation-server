package airlineReservation.domain.common.serviceOutput;

import airlineReservation.infra.entity.ScheduleSeat;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetSeatServiceOutput {

    private final Integer scheduleId;
    private final List<ScheduleSeat> seatList;

}
