package airlineReservation.domain.booking.serviceMapper;

import airlineReservation.domain.booking.serviceInput.CreateBookingServiceInput;
import airlineReservation.domain.booking.serviceOutput.CreateBookingServiceOutput;
import airlineReservation.infra.dto.CreateBookingRequest;
import airlineReservation.infra.dto.CreateBookingResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateBookingServiceMapper {

    public CreateBookingServiceInput toServiceInput(CreateBookingRequest request) {
        if (request == null) {
            return null;
        }

        return CreateBookingServiceInput.builder()
                .userId(request.getUserId())
                .scheduleId(request.getScheduleId())
                .totalPrice(request.getTotalPrice())
                .passengerList(request.getPassengerList())
                .build();
    }

    public CreateBookingResponse toResponse(CreateBookingServiceOutput output) {
        CreateBookingResponse response = new CreateBookingResponse();
        response.setBookingId(output.getBookingId());
        return response;
    }
}
