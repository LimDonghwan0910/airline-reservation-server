package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.UpdateAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.UpdateAircraftServiceOutput;
import airlineReservation.domain.admin.validator.AircraftInputValidator;
import airlineReservation.domain.admin.vo.AircraftDeletionConstraintVo;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.ConflictException;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.entity.Aircraft;
import airlineReservation.infra.mapper.AircraftMapper;
import airlineReservation.infra.mapper.customMapper.AircraftCustomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 航空機更新処理を行うサービス。
 */
@Service
@RequiredArgsConstructor
public class UpdateAircraftService {

    private final AircraftMapper aircraftMapper;
    private final AircraftCustomMapper aircraftCustomMapper;
    private final AircraftInputValidator aircraftInputValidator;

    /**
     * 航空機情報を更新する。
     *
     * @param input
     * @return serviceOutput
     * @throws InvalidInputValueException 入力項目が誤っている場合
     * @throws NotFoundException 対象航空機が存在しない、または削除済みの場合
     * @throws ConflictException 有効な予約、運航スケジュール、または定期運航テンプレートが残っている場合
     */
    @Transactional
    public UpdateAircraftServiceOutput update(UpdateAircraftServiceInput input) {
        aircraftInputValidator.validate(
                input.getAircraftId(),
                input.getAircraftName(),
                input.getRowCount(),
                input.getColumnCount()
        );

        Aircraft existing = aircraftMapper.selectByPrimaryKey(input.getAircraftId());
        if (existing == null || Boolean.TRUE.equals(existing.getIsDeleted())) {
            throw new NotFoundException(
                    ErrorCode.AIRCRAFT_NOT_FOUND,
                    "登録されていない航空機です: " + input.getAircraftId()
            );
        }

        ensureUpdatable(input.getAircraftId());

        Aircraft entity = new Aircraft();
        entity.setAircraftId(input.getAircraftId());
        entity.setAircraftName(input.getAircraftName());
        entity.setRowCount(input.getRowCount());
        entity.setColumnCount(input.getColumnCount());

        aircraftMapper.updateByPrimaryKeySelective(entity);

        return UpdateAircraftServiceOutput.builder()
                .build();
    }

    /**
     * 航空機に予約・運航スケジュール・定期運航テンプレートが残っていないことを確認する。
     *
     * @param aircraftId チェック対象の航空機ID
     * @throws ConflictException 更新を妨げる参照が存在する場合
     */
    private void ensureUpdatable(String aircraftId) {
        AircraftDeletionConstraintVo constraint = aircraftCustomMapper.selectDeletionConstraints(
                aircraftId,
                Const.BOOKING_STATUS.CANCELLED,
                Const.SCHEDULE_STATUS.CANCELLED
        );

        if (constraint.getActiveBookingCount() > 0) {
            throw new ConflictException(ErrorCode.AIRCRAFT_HAS_ACTIVE_BOOKINGS);
        }
        if (constraint.getActiveScheduleCount() > 0) {
            throw new ConflictException(
                    ErrorCode.AIRCRAFT_HAS_ACTIVE_SCHEDULES,
                    "運航スケジュールが存在するため航空機を更新できません。"
            );
        }
        if (constraint.getScheduleTemplateCount() > 0) {
            throw new ConflictException(
                    ErrorCode.AIRCRAFT_HAS_SCHEDULE_TEMPLATE,
                    "定期運航テンプレートが存在するため航空機を更新できません。"
            );
        }
    }
}
