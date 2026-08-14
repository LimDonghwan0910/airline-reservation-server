package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.DeleteScheduleServiceInput;
import airlineReservation.domain.admin.serviceOutput.DeleteScheduleServiceOutput;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.entity.Schedule;
import airlineReservation.infra.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 運航スケジュール削除処理を行うサービス。
 */
@Service
@RequiredArgsConstructor
public class DeleteScheduleService {

    private final ScheduleMapper scheduleMapper;
    private final ScheduleSeatProvisioningService scheduleSeatProvisioningService;

    /**
     * 運航スケジュールをキャンセルする。
     *
     * @param input
     * @return serviceOutput
     * @throws InvalidInputValueException 入力項目が誤っている場合
     * @throws NotFoundException 対象スケジュールが存在しない場合
     */
    @Transactional
    public DeleteScheduleServiceOutput delete(DeleteScheduleServiceInput input) {
        if (input.getScheduleId() == null) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "スケジュールIDを入力してください。");
        }

        Schedule schedule = scheduleMapper.selectByPrimaryKey(input.getScheduleId());
        if (schedule == null) {
            throw new NotFoundException(
                    ErrorCode.SCHEDULE_NOT_FOUND,
                    "存在しない運航スケジュールです: " + input.getScheduleId()
            );
        }

        Schedule update = new Schedule();
        update.setScheduleId(input.getScheduleId());
        update.setStatus(Const.SCHEDULE_STATUS.CANCELLED);
        scheduleMapper.updateByPrimaryKeySelective(update);
        scheduleSeatProvisioningService.cancelForSchedule(input.getScheduleId());

        return DeleteScheduleServiceOutput.builder()
                .build();
    }
}
