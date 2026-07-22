package airlineReservation.domain.admin.controller;

import airlineReservation.domain.admin.service.GetAirportService;
import airlineReservation.domain.admin.serviceMapper.GetAirportServiceMapper;
import airlineReservation.domain.admin.serviceOutput.GetAirportServiceOutput;
import airlineReservation.domain.dto.GetAirportsResponse;
import airlineReservation.global.constant.ApiEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AirportController {

    private final GetAirportService service;
    private final GetAirportServiceMapper mapper;

    @GetMapping(ApiEndpoints.Common.GET_AIRPORTS)
    public ResponseEntity<GetAirportsResponse> getAirports() {

        GetAirportServiceOutput serviceOutput = service.getAirportList();
        GetAirportsResponse response = mapper.toResponse(serviceOutput);
        return ResponseEntity.ok(response);
    }

}
