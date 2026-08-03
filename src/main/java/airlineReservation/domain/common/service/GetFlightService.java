package airlineReservation.domain.common.service;

import airlineReservation.domain.common.serviceInput.GetFlightServiceInput;
import airlineReservation.domain.common.serviceOutput.GetFlightServiceOutput;
import airlineReservation.domain.common.vo.GetFlightVo;
import airlineReservation.infra.dto.GetFlightsResponseFlightsListInner;
import airlineReservation.infra.mapper.customMapper.GetFlightCustomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetFlightService {

    private final GetFlightCustomMapper getFlightCustomMapper;

    public GetFlightServiceOutput getFlightList(GetFlightServiceInput input) {
        LocalDateTime start = input.getDepartureDate().atStartOfDay();           // 2026-07-25 00:00:00
        LocalDateTime end   = input.getDepartureDate().plusDays(1).atStartOfDay();

        List<GetFlightVo> scheduleList = getFlightCustomMapper.selectFlightList(
                input.getDepartureAirportId(),
                input.getArrivalAirportId(),
                start,
                end
        );

        List<GetFlightsResponseFlightsListInner> flightList = new ArrayList<>();

        for (GetFlightVo schedule : scheduleList) {
            GetFlightsResponseFlightsListInner item = new GetFlightsResponseFlightsListInner();

            item.setScheduleId(schedule.getScheduleId());
            item.setAircraftId(schedule.getAircraftId());
            item.setDepartureAirportId(schedule.getDepartureAirportId());
            item.setArrivalAirportId(schedule.getArrivalAirportId());
            item.setDepartureDatetime(schedule.getDepartureDatetime());
            item.setArrivalDatetime(schedule.getArrivalDatetime());
            item.setPrice(schedule.getPrice());
            item.setAircraftName(schedule.getAircraftName());

            flightList.add(item);
        }

        return GetFlightServiceOutput.builder()
                .flightList(flightList)
                .build();
    }
}
