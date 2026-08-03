package airlineReservation.domain.common.controller;

import airlineReservation.domain.common.service.GetFlightService;
import airlineReservation.domain.common.serviceInput.GetFlightServiceInput;
import airlineReservation.domain.common.serviceMapper.GetFlightServiceMapper;
import airlineReservation.domain.common.serviceOutput.GetFlightServiceOutput;
import airlineReservation.infra.dto.GetFlightsRequest;
import airlineReservation.infra.dto.GetFlightsResponse;
import airlineReservation.global.constant.ApiEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FlightController {

    private final GetFlightService service;
    private final GetFlightServiceMapper mapper;

    @GetMapping(ApiEndpoints.Common.GET_FLIGHTS)
    public ResponseEntity<GetFlightsResponse> getFlights(@ModelAttribute GetFlightsRequest request) {

        GetFlightServiceInput serviceInput = mapper.toServiceInput(request);

        GetFlightServiceOutput serviceOutput = service.getFlightList(serviceInput);

        GetFlightsResponse response = mapper.toResponse(serviceOutput);

        return ResponseEntity.ok(response);
    }

}
