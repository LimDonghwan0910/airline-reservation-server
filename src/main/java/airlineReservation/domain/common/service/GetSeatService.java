package airlineReservation.domain.common.service;

import airlineReservation.domain.common.serviceInput.GetSeatServiceInput;
import airlineReservation.domain.common.serviceOutput.GetSeatServiceOutput;
import airlineReservation.infra.entity.ScheduleSeat;
import airlineReservation.infra.entity.ScheduleSeatExample;
import airlineReservation.infra.mapper.ScheduleSeatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetSeatService {

    private final ScheduleSeatMapper scheduleSeatMapper;

    public GetSeatServiceOutput getSeatList(GetSeatServiceInput input) {
        if (input.getScheduleId() == null) {
            return GetSeatServiceOutput.builder()
                    .scheduleId(input.getScheduleId())
                    .seatList(Collections.emptyList())
                    .build();
        }

        ScheduleSeatExample example = new ScheduleSeatExample();
        example.createCriteria().andScheduleIdEqualTo(input.getScheduleId());
        example.setOrderByClause("seat_name ASC");

        List<ScheduleSeat> seatList = scheduleSeatMapper.selectByExample(example);

        return GetSeatServiceOutput.builder()
                .scheduleId(input.getScheduleId())
                .seatList(seatList)
                .build();
    }
}
