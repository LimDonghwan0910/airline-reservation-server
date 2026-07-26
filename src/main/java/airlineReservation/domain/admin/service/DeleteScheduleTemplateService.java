package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.DeleteScheduleTemplateServiceInput;
import airlineReservation.domain.admin.serviceOutput.DeleteScheduleTemplateServiceOutput;
import airlineReservation.infra.entity.Schedule;
import airlineReservation.infra.entity.ScheduleExample;
import airlineReservation.infra.entity.ScheduleTemplates;
import airlineReservation.infra.mapper.ScheduleMapper;
import airlineReservation.infra.mapper.ScheduleTemplatesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteScheduleTemplateService {

    private static final String STATUS_CANCELLED = "CANCELLED";

    private final ScheduleTemplatesMapper scheduleTemplatesMapper;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleSeatProvisioningService scheduleSeatProvisioningService;

    @Transactional
    public DeleteScheduleTemplateServiceOutput delete(DeleteScheduleTemplateServiceInput input) {
        if (input.getTemplateId() == null) {
            throw new IllegalArgumentException("템플릿 ID를 입력해 주세요.");
        }

        ScheduleTemplates template = scheduleTemplatesMapper.selectByPrimaryKey(input.getTemplateId());
        if (template == null) {
            throw new IllegalArgumentException("존재하지 않는 정기 운항 템플릿입니다: " + input.getTemplateId());
        }

        cancelLinkedSchedules(input.getTemplateId());
        scheduleTemplatesMapper.deleteByPrimaryKey(input.getTemplateId());

        return DeleteScheduleTemplateServiceOutput.builder()
                .build();
    }

    private void cancelLinkedSchedules(Integer templateId) {
        ScheduleExample example = new ScheduleExample();
        example.createCriteria().andTemplateIdEqualTo(templateId);

        List<Schedule> linkedSchedules = scheduleMapper.selectByExample(example);
        for (Schedule schedule : linkedSchedules) {
            Schedule update = new Schedule();
            update.setScheduleId(schedule.getScheduleId());
            update.setStatus(STATUS_CANCELLED);
            scheduleMapper.updateByPrimaryKeySelective(update);
            scheduleSeatProvisioningService.cancelForSchedule(schedule.getScheduleId());
        }
    }
}
