package airlineReservation.domain.common.serviceMapper;

import airlineReservation.domain.common.serviceInput.GetFlightServiceInput;
import airlineReservation.domain.common.serviceOutput.GetFlightServiceOutput;
import airlineReservation.infra.dto.GetFlightsRequest;
import airlineReservation.infra.dto.GetFlightsResponse;
import org.springframework.stereotype.Component;

@Component
public class GetFlightServiceMapper {

    public GetFlightServiceInput toServiceInput(GetFlightsRequest request) {
        if (request == null) {
            return null;
        }

        return GetFlightServiceInput.builder()
                .departureAirportId(request.getDepartureAirportId())
                .arrivalAirportId(request.getArrivalAirportId())
                .departureDate(request.getDepartureDate())
                .passengerCount(request.getPassengerCount())
                .build();
    }

    public GetFlightsResponse toResponse(GetFlightServiceOutput output) {
        if (output == null) {
            return null;
        }

        GetFlightsResponse response = new GetFlightsResponse();
        response.setFlightsList(output.getFlightList());
        return response;
    }
}
