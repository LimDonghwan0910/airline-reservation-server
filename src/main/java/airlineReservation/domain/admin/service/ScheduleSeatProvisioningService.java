package airlineReservation.domain.admin.service;

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

    private static final String STATUS_AVAILABLE = "AVAILABLE";
    private static final String STATUS_CANCELLED = "CANCELLED";

    private final AircraftMapper aircraftMapper;
    private final ScheduleSeatMapper scheduleSeatMapper;

    /**
     * schedule_id に対応する航空機の全座席を AVAILABLE 状態で登録する。
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
            scheduleSeat.setStatus(STATUS_AVAILABLE);
            scheduleSeatMapper.insertSelective(scheduleSeat);
        }
    }

    /**
     * schedule_id に紐づく全座席の状態を CANCELLED に変更する。
     */
    public void cancelForSchedule(Integer scheduleId) {
        ScheduleSeat update = new ScheduleSeat();
        update.setStatus(STATUS_CANCELLED);

        ScheduleSeatExample example = new ScheduleSeatExample();
        example.createCriteria().andScheduleIdEqualTo(scheduleId);

        scheduleSeatMapper.updateByExampleSelective(update, example);
    }

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
