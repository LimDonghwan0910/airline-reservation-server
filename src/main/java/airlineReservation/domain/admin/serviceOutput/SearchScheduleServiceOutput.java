package airlineReservation.domain.admin.serviceOutput;

import airlineReservation.infra.entity.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchScheduleServiceOutput {

    private final List<Schedule> scheduleList;

}
