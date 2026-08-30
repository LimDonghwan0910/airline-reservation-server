package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.DeleteScheduleTemplateServiceInput;
import airlineReservation.domain.admin.serviceOutput.DeleteScheduleTemplateServiceOutput;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.ConflictException;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.entity.Schedule;
import airlineReservation.infra.entity.ScheduleExample;
import airlineReservation.infra.entity.ScheduleTemplates;
import airlineReservation.infra.mapper.ScheduleMapper;
import airlineReservation.infra.mapper.ScheduleTemplatesMapper;
import airlineReservation.infra.mapper.customMapper.ScheduleTemplateCustomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 定期運航テンプレート削除処理を行うサービス。
 */
@Service
@RequiredArgsConstructor
public class DeleteScheduleTemplateService {

    private final ScheduleTemplatesMapper scheduleTemplatesMapper;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleTemplateCustomMapper scheduleTemplateCustomMapper;
    private final ScheduleSeatProvisioningService scheduleSeatProvisioningService;

    /**
     * 定期運航テンプレートを削除し、紐づくスケジュールをキャンセルする。
     * 削除可否判定の前にテンプレートと紐づくスケジュールをロックする。
     *
     * @param input
     * @return serviceOutput
     * @throws InvalidInputValueException 入力項目が誤っている場合
     * @throws NotFoundException 対象テンプレートが存在しない場合
     * @throws ConflictException 紐づくスケジュールに有効な予約が残っている場合
     */
    @Transactional
    public DeleteScheduleTemplateServiceOutput delete(DeleteScheduleTemplateServiceInput input) {
        if (input.getTemplateId() == null) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "テンプレートIDを入力してください。");
        }

        ScheduleTemplates template = scheduleTemplateCustomMapper.selectByIdForUpdate(input.getTemplateId());
        if (template == null) {
            throw new NotFoundException(
                    ErrorCode.TEMPLATE_NOT_FOUND,
                    "存在しない定期運航テンプレートです: " + input.getTemplateId()
            );
        }

        scheduleTemplateCustomMapper.lockLinkedSchedules(input.getTemplateId());
        ensureDeletable(input.getTemplateId());

        cancelLinkedSchedules(input.getTemplateId());
        scheduleTemplatesMapper.deleteByPrimaryKey(input.getTemplateId());

        return DeleteScheduleTemplateServiceOutput.builder()
                .build();
    }

    /**
     * テンプレートに紐づくスケジュールに有効な予約が残っていないことを確認する。
     *
     * @param templateId チェック対象のテンプレートID
     * @throws ConflictException 有効な予約が存在する場合
     */
    private void ensureDeletable(Integer templateId) {
        long activeBookingCount = scheduleTemplateCustomMapper.countActiveBookings(
                templateId,
                Const.BOOKING_STATUS.CANCELLED
        );

        if (activeBookingCount > 0) {
            throw new ConflictException(ErrorCode.SCHEDULE_TEMPLATE_HAS_ACTIVE_BOOKINGS);
        }
    }

    /**
     * テンプレートに紐づく全スケジュールをキャンセルする。
     *
     * @param templateId 対象テンプレートID
     */
    private void cancelLinkedSchedules(Integer templateId) {
        ScheduleExample example = new ScheduleExample();
        example.createCriteria().andTemplateIdEqualTo(templateId);

        List<Schedule> linkedSchedules = scheduleMapper.selectByExample(example);
        for (Schedule schedule : linkedSchedules) {
            Schedule update = new Schedule();
            update.setScheduleId(schedule.getScheduleId());
            update.setStatus(Const.SCHEDULE_STATUS.CANCELLED);
            scheduleMapper.updateByPrimaryKeySelective(update);
            scheduleSeatProvisioningService.cancelForSchedule(schedule.getScheduleId());
        }
    }
}
