package airlineReservation.domain.admin.serviceOutput;

import airlineReservation.domain.entity.Aircraft;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchAircraftServiceOutput {
    private final List<Aircraft> aircraftList;
}
