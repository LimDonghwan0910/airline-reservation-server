package airlineReservation.domain.admin.serviceInput;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class SearchScheduleServiceInput {

    private final String aircraftId;
    private final String departureAirportId;
    private final String arrivalAirportId;
    private final LocalDate departureDate;

}
