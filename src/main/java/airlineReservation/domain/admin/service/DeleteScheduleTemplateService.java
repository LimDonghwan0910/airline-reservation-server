package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.DeleteScheduleTemplateServiceInput;
import airlineReservation.domain.admin.serviceOutput.DeleteScheduleTemplateServiceOutput;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
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
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "テンプレートIDを入力してください。");
        }

        ScheduleTemplates template = scheduleTemplatesMapper.selectByPrimaryKey(input.getTemplateId());
        if (template == null) {
            throw new NotFoundException(
                    ErrorCode.TEMPLATE_NOT_FOUND,
                    "存在しない定期運航テンプレートです: " + input.getTemplateId()
            );
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
