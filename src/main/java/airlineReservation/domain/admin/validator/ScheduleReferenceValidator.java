package airlineReservation.domain.admin.validator;

import airlineReservation.infra.entity.Aircraft;
import airlineReservation.infra.entity.AircraftExample;
import airlineReservation.infra.entity.Airport;
import airlineReservation.infra.entity.AirportExample;
import airlineReservation.infra.mapper.AircraftMapper;
import airlineReservation.infra.mapper.AirportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class ScheduleReferenceValidator {

    private final AircraftMapper aircraftMapper;
    private final AirportMapper airportMapper;

    public void validateAircraft(String aircraftId) {
        if (!StringUtils.hasText(aircraftId)) {
            throw new IllegalArgumentException("편명(항공기)을 선택해 주세요.");
        }

        AircraftExample example = new AircraftExample();
        example.createCriteria()
                .andAircraftIdEqualTo(aircraftId)
                .andIsDeletedEqualTo(false);

        Aircraft aircraft = aircraftMapper.selectByExample(example).stream()
                .findFirst()
                .orElse(null);

        if (aircraft == null) {
            throw new IllegalArgumentException("등록되지 않은 항공기입니다: " + aircraftId);
        }
    }

    public void validateAirport(String airportId) {
        if (!StringUtils.hasText(airportId)) {
            throw new IllegalArgumentException("공항을 선택해 주세요.");
        }

        AirportExample example = new AirportExample();
        example.createCriteria()
                .andAirportIdEqualTo(airportId)
                .andIsDeletedEqualTo(false);

        Airport airport = airportMapper.selectByExample(example).stream()
                .findFirst()
                .orElse(null);

        if (airport == null) {
            throw new IllegalArgumentException("등록되지 않은 공항입니다: " + airportId);
        }
    }
}
