package airlineReservation.domain.common.service;

import airlineReservation.domain.common.serviceInput.GetSeatServiceInput;
import airlineReservation.domain.common.serviceOutput.GetSeatServiceOutput;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.entity.Schedule;
import airlineReservation.infra.entity.ScheduleSeat;
import airlineReservation.infra.entity.ScheduleSeatExample;
import airlineReservation.infra.mapper.ScheduleMapper;
import airlineReservation.infra.mapper.ScheduleSeatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 座席一覧取得処理を行うサービス。
 */
@Service
@RequiredArgsConstructor
public class GetSeatService {

    private final ScheduleSeatMapper scheduleSeatMapper;
    private final ScheduleMapper scheduleMapper;

    /**
     * 指定スケジュールの座席一覧を取得する。
     *
     * @param input
     * @return serviceOutput
     * @throws InvalidInputValueException 入力項目が誤っている場合
     * @throws NotFoundException スケジュールが存在しない場合
     */
    public GetSeatServiceOutput getSeatList(GetSeatServiceInput input) {
        validateInput(input);

        ScheduleSeatExample example = new ScheduleSeatExample();
        example.createCriteria().andScheduleIdEqualTo(input.getScheduleId());
        example.setOrderByClause("seat_name ASC");

        List<ScheduleSeat> seatList = scheduleSeatMapper.selectByExample(example);

        return GetSeatServiceOutput.builder()
                .scheduleId(input.getScheduleId())
                .seatList(seatList)
                .build();
    }

    /**
     * 入力値チェックを行う。
     *
     * @param input
     * @throws InvalidInputValueException
     * @throws NotFoundException
     */
    private void validateInput(GetSeatServiceInput input) {
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
        if (Const.BOOKING_STATUS.CANCELLED.equals(schedule.getStatus())) {
            throw new InvalidInputValueException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "キャンセルされた運航スケジュールです: " + input.getScheduleId()
            );
        }
    }
}
