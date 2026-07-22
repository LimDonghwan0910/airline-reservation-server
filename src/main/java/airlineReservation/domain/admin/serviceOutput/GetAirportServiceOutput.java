package airlineReservation.domain.admin.serviceOutput;

import airlineReservation.domain.entity.Airport;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetAirportServiceOutput {
    private final List<Airport> airportList;
}
