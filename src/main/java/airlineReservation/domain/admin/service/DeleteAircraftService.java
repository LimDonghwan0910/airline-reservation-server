package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.DeleteAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.DeleteAircraftServiceOutput;
import airlineReservation.domain.admin.vo.AircraftDeletionConstraintVo;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.ConflictException;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.entity.Aircraft;
import airlineReservation.infra.mapper.AircraftMapper;
import airlineReservation.infra.mapper.customMapper.AircraftCustomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 航空機削除処理を行うサービス。
 */
@Service
@RequiredArgsConstructor
public class DeleteAircraftService {

    private final AircraftMapper aircraftMapper;
    private final AircraftCustomMapper aircraftCustomMapper;

    /**
     * 航空機を論理削除する。
     *
     * @param input
     * @return serviceOutput
     * @throws InvalidInputValueException 入力項目が誤っている場合
     * @throws NotFoundException 対象航空機が存在しない、または削除済みの場合
     * @throws ConflictException 有効な予約、運航スケジュール、または定期運航テンプレートが残っている場合
     */
    @Transactional
    public DeleteAircraftServiceOutput delete(DeleteAircraftServiceInput input) {
        if (!StringUtils.hasText(input.getAircraftId())) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "航空機IDを入力してください。");
        }

        Aircraft existing = aircraftMapper.selectByPrimaryKey(input.getAircraftId());
        if (existing == null || Boolean.TRUE.equals(existing.getIsDeleted())) {
            throw new NotFoundException(
                    ErrorCode.AIRCRAFT_NOT_FOUND,
                    "登録されていない航空機です: " + input.getAircraftId()
            );
        }

        ensureDeletable(input.getAircraftId());

        Aircraft entity = new Aircraft();
        entity.setAircraftId(input.getAircraftId());
        entity.setIsDeleted(true);
        aircraftMapper.updateByPrimaryKeySelective(entity);

        return DeleteAircraftServiceOutput.builder()
                .build();
    }

    /**
     * 航空機に予約・運航スケジュール・定期運航テンプレートが残っていないことを確認する。
     *
     * @param aircraftId チェック対象の航空機ID
     * @throws ConflictException 削除を妨げる参照が存在する場合
     */
    private void ensureDeletable(String aircraftId) {
        AircraftDeletionConstraintVo constraint = aircraftCustomMapper.selectDeletionConstraints(
                aircraftId,
                Const.BOOKING_STATUS.CANCELLED,
                Const.SCHEDULE_STATUS.CANCELLED
        );

        if (constraint.getActiveBookingCount() > 0) {
            throw new ConflictException(
                    ErrorCode.AIRCRAFT_HAS_ACTIVE_BOOKINGS,
                    "有効な予約があるため航空機を削除できません。"
            );
        }
        if (constraint.getActiveScheduleCount() > 0) {
            throw new ConflictException(ErrorCode.AIRCRAFT_HAS_ACTIVE_SCHEDULES);
        }
        if (constraint.getScheduleTemplateCount() > 0) {
            throw new ConflictException(ErrorCode.AIRCRAFT_HAS_SCHEDULE_TEMPLATE);
        }
    }
}
