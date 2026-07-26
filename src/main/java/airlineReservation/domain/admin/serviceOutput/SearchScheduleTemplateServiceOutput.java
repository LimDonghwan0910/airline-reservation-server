package airlineReservation.domain.admin.serviceOutput;

import airlineReservation.infra.entity.ScheduleTemplates;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchScheduleTemplateServiceOutput {

    private final List<ScheduleTemplates> scheduleTemplateList;

}
