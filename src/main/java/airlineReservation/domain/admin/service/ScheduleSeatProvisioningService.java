package airlineReservation.domain.admin.service;

import airlineReservation.infra.entity.Aircraft;
import airlineReservation.infra.entity.AircraftExample;
import airlineReservation.infra.entity.ScheduleSeat;
import airlineReservation.infra.entity.ScheduleSeatExample;
import airlineReservation.global.util.SeatLayoutUtil;
import airlineReservation.infra.mapper.AircraftMapper;
import airlineReservation.infra.mapper.ScheduleSeatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 운항 일정별 좌석 상태(schedule_seats) 생성·취소 서비스.
 */
@Service
@RequiredArgsConstructor
public class ScheduleSeatProvisioningService {

    private static final String STATUS_AVAILABLE = "AVAILABLE";
    private static final String STATUS_CANCELLED = "CANCELLED";

    private final AircraftMapper aircraftMapper;
    private final ScheduleSeatMapper scheduleSeatMapper;

    /**
     * schedule_id에 해당 항공기의 모든 좌석을 AVAILABLE 상태로 등록한다.
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
     * schedule_id에 연결된 모든 좌석 상태를 CANCELLED로 변경한다.
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
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 항공기입니다: " + aircraftId));
    }
}
