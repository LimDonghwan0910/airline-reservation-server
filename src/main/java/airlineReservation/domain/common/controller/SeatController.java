package airlineReservation.domain.common.controller;

import airlineReservation.domain.common.service.GetSeatService;
import airlineReservation.domain.common.serviceInput.GetSeatServiceInput;
import airlineReservation.domain.common.serviceMapper.GetSeatServiceMapper;
import airlineReservation.domain.common.serviceOutput.GetSeatServiceOutput;
import airlineReservation.infra.dto.GetSeatsRequest;
import airlineReservation.infra.dto.GetSeatsResponse;
import airlineReservation.global.constant.ApiEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SeatController {

    private final GetSeatService service;
    private final GetSeatServiceMapper mapper;

    @GetMapping(ApiEndpoints.Common.GET_SEATS)
    public ResponseEntity<GetSeatsResponse> getSeats(@ModelAttribute GetSeatsRequest request) {

        GetSeatServiceInput serviceInput = mapper.toServiceInput(request);

        GetSeatServiceOutput serviceOutput = service.getSeatList(serviceInput);

        GetSeatsResponse response = mapper.toResponse(serviceOutput);

        return ResponseEntity.ok(response);
    }

}
