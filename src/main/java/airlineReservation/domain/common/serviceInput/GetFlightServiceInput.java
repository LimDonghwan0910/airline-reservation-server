package airlineReservation.domain.common.serviceInput;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class GetFlightServiceInput {

    private final String departureAirportId;
    private final String arrivalAirportId;
    private final LocalDate departureDate;
    private final Integer passengerCount;

}
