package airlineReservation.domain.common.serviceOutput;

import airlineReservation.infra.dto.GetFlightsResponseFlightsListInner;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetFlightServiceOutput {

    private final List<GetFlightsResponseFlightsListInner> flightList;

}
