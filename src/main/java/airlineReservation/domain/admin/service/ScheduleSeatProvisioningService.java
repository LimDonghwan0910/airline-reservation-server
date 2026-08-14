package airlineReservation.domain.admin.service;

import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.global.util.SeatLayoutUtil;
import airlineReservation.infra.entity.Aircraft;
import airlineReservation.infra.entity.AircraftExample;
import airlineReservation.infra.entity.ScheduleSeat;
import airlineReservation.infra.entity.ScheduleSeatExample;
import airlineReservation.infra.mapper.AircraftMapper;
import airlineReservation.infra.mapper.ScheduleSeatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 運航スケジュールごとの座席状態（schedule_seats）を作成・キャンセルするサービス。
 */
@Service
@RequiredArgsConstructor
public class ScheduleSeatProvisioningService {

    private final AircraftMapper aircraftMapper;
    private final ScheduleSeatMapper scheduleSeatMapper;

    /**
     * 指定スケジュールの全座席を AVAILABLE 状態で登録する。
     *
     * @param scheduleId 対象スケジュールID
     * @param aircraftId 座席レイアウト参照用の航空機ID
     * @throws NotFoundException 航空機が存在しない場合
     */
    public void provisionForSchedule(Integer scheduleId, String aircraftId) {
        Aircraft aircraft = findActiveAircraft(aircraftId);
        List<String> seatNames = SeatLayoutUtil.generateSeatNames(
                aircraft.getRowCount(),
                aircraft.getColumnCount()
        );

        for (String seatName : seatNames) {
            ScheduleSeat scheduleSeat = new ScheduleSeat();
            scheduleSeat.setScheduleId(scheduleId);
            scheduleSeat.setSeatName(seatName);
            scheduleSeat.setStatus(Const.SEAT_STATUS.AVAILABLE);
            scheduleSeatMapper.insertSelective(scheduleSeat);
        }
    }

    /**
     * 指定スケジュールの全座席状態を CANCELLED に変更する。
     *
     * @param scheduleId 対象スケジュールID
     */
    public void cancelForSchedule(Integer scheduleId) {
        ScheduleSeat update = new ScheduleSeat();
        update.setStatus(Const.SEAT_STATUS.CANCELLED);

        ScheduleSeatExample example = new ScheduleSeatExample();
        example.createCriteria().andScheduleIdEqualTo(scheduleId);

        scheduleSeatMapper.updateByExampleSelective(update, example);
    }

    /**
     * 有効な航空機を取得する。
     *
     * @param aircraftId チェック対象の航空機ID
     * @return 航空機エンティティ
     * @throws NotFoundException 航空機が存在しない場合
     */
    private Aircraft findActiveAircraft(String aircraftId) {
        AircraftExample example = new AircraftExample();
        example.createCriteria()
                .andAircraftIdEqualTo(aircraftId)
                .andIsDeletedEqualTo(false);

        return aircraftMapper.selectByExample(example).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.AIRCRAFT_NOT_FOUND,
                        "登録されていない航空機です: " + aircraftId
                ));
    }
}
