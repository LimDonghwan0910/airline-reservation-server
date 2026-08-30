package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.DeleteScheduleServiceInput;
import airlineReservation.domain.admin.serviceOutput.DeleteScheduleServiceOutput;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.ConflictException;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.entity.BookingExample;
import airlineReservation.infra.entity.Schedule;
import airlineReservation.infra.mapper.BookingMapper;
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
    private final BookingMapper bookingMapper;
    private final ScheduleSeatProvisioningService scheduleSeatProvisioningService;

    /**
     * 運航スケジュールをキャンセルする。
     *
     * @param input
     * @return serviceOutput
     * @throws InvalidInputValueException 入力項目が誤っている場合
     * @throws NotFoundException 対象スケジュールが存在しない場合
     * @throws ConflictException 有効な予約が残っている場合
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

        ensureDeletable(input.getScheduleId());

        Schedule update = new Schedule();
        update.setScheduleId(input.getScheduleId());
        update.setStatus(Const.SCHEDULE_STATUS.CANCELLED);
        scheduleMapper.updateByPrimaryKeySelective(update);
        scheduleSeatProvisioningService.cancelForSchedule(input.getScheduleId());

        return DeleteScheduleServiceOutput.builder()
                .build();
    }

    /**
     * 対象スケジュールに有効な予約が残っていないことを確認する。
     *
     * @param scheduleId チェック対象のスケジュールID
     * @throws ConflictException 有効な予約が存在する場合
     */
    private void ensureDeletable(Integer scheduleId) {
        BookingExample example = new BookingExample();
        example.createCriteria()
                .andScheduleIdEqualTo(scheduleId)
                .andIsDeletedEqualTo(false)
                .andStatusNotEqualTo(Const.BOOKING_STATUS.CANCELLED);

        if (bookingMapper.countByExample(example) > 0) {
            throw new ConflictException(ErrorCode.SCHEDULE_HAS_ACTIVE_BOOKINGS);
        }
    }
}
