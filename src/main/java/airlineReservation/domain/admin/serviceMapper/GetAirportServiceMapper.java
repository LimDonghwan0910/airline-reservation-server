package airlineReservation.domain.admin.serviceMapper;

import airlineReservation.domain.admin.serviceOutput.GetAirportServiceOutput;
import airlineReservation.domain.dto.GetAirportsResponse;
import airlineReservation.domain.dto.GetAirportsResponseAirportListInner;
import airlineReservation.domain.entity.Airport;
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
