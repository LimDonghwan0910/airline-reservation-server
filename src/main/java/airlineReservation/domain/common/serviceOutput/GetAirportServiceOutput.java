package airlineReservation.domain.common.serviceOutput;

import airlineReservation.infra.entity.Airport;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetAirportServiceOutput {
    private final List<Airport> airportList;
}
