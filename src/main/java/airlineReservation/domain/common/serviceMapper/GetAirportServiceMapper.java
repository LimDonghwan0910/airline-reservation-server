package airlineReservation.domain.common.serviceMapper;

import airlineReservation.domain.common.serviceOutput.GetAirportServiceOutput;
import airlineReservation.infra.dto.GetAirportsResponse;
import airlineReservation.infra.dto.GetAirportsResponseAirportListInner;
import airlineReservation.infra.entity.Airport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetAirportServiceMapper {

    public GetAirportsResponse toResponse(GetAirportServiceOutput output) {
        List<GetAirportsResponseAirportListInner> airportList = new ArrayList<>();
        GetAirportsResponse response = new GetAirportsResponse();

        if (output.getAirportList() != null) {
            for (Airport airport : output.getAirportList()) {
                GetAirportsResponseAirportListInner item = new GetAirportsResponseAirportListInner();
                item.setAirportId(airport.getAirportId());
                item.setAirportNameKo(airport.getAirportNameKo());
                item.setAirportNameEn(airport.getAirportNameEn());
                item.setCountry(airport.getCountry());
                item.setCity(airport.getCity());
                airportList.add(item);
            }
        }

        response.setAirportList(airportList);
        return response;
    }
}
