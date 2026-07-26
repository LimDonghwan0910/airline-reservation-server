package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.DeleteScheduleServiceInput;
import airlineReservation.domain.admin.serviceOutput.DeleteScheduleServiceOutput;
import airlineReservation.infra.entity.Schedule;
import airlineReservation.infra.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteScheduleService {

    private static final String STATUS_CANCELLED = "CANCELLED";

    private final ScheduleMapper scheduleMapper;
    private final ScheduleSeatProvisioningService scheduleSeatProvisioningService;

    @Transactional
    public DeleteScheduleServiceOutput delete(DeleteScheduleServiceInput input) {
        if (input.getScheduleId() == null) {
            throw new IllegalArgumentException("스케줄 ID를 입력해 주세요.");
        }

        Schedule schedule = scheduleMapper.selectByPrimaryKey(input.getScheduleId());
        if (schedule == null) {
            throw new IllegalArgumentException("존재하지 않는 운항 일정입니다: " + input.getScheduleId());
        }

        Schedule update = new Schedule();
        update.setScheduleId(input.getScheduleId());
        update.setStatus(STATUS_CANCELLED);
        scheduleMapper.updateByPrimaryKeySelective(update);
        scheduleSeatProvisioningService.cancelForSchedule(input.getScheduleId());

        return DeleteScheduleServiceOutput.builder()
                .build();
    }
}
