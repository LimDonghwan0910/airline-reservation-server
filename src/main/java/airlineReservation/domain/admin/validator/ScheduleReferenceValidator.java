package airlineReservation.domain.admin.validator;

import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
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
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "便名（航空機）を選択してください。");
        }

        AircraftExample example = new AircraftExample();
        example.createCriteria()
                .andAircraftIdEqualTo(aircraftId)
                .andIsDeletedEqualTo(false);

        Aircraft aircraft = aircraftMapper.selectByExample(example).stream()
                .findFirst()
                .orElse(null);

        if (aircraft == null) {
            throw new NotFoundException(ErrorCode.AIRCRAFT_NOT_FOUND, "登録されていない航空機です: " + aircraftId);
        }
    }

    public void validateAirport(String airportId) {
        if (!StringUtils.hasText(airportId)) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "空港を選択してください。");
        }

        AirportExample example = new AirportExample();
        example.createCriteria()
                .andAirportIdEqualTo(airportId)
                .andIsDeletedEqualTo(false);

        Airport airport = airportMapper.selectByExample(example).stream()
                .findFirst()
                .orElse(null);

        if (airport == null) {
            throw new NotFoundException(ErrorCode.AIRPORT_NOT_FOUND, "登録されていない空港です: " + airportId);
        }
    }
}
