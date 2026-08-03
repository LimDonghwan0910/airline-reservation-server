package airlineReservation.domain.booking.serviceMapper;

import airlineReservation.domain.booking.serviceInput.DeleteBookingServiceInput;
import airlineReservation.domain.booking.serviceOutput.DeleteBookingServiceOutput;
import airlineReservation.infra.dto.DeleteBookingRequest;
import airlineReservation.infra.dto.DeleteBookingResponse;
import org.springframework.stereotype.Component;

@Component
public class DeleteBookingServiceMapper {

    public DeleteBookingServiceInput toServiceInput(DeleteBookingRequest request) {
        if (request == null) {
            return null;
        }

        return DeleteBookingServiceInput.builder()
                .bookingId(request.getBookingId())
                .build();
    }

    public DeleteBookingServiceInput toServiceInput(DeleteBookingRequest request, Integer updatedBy) {
        DeleteBookingServiceInput input = toServiceInput(request);
        if (input == null) {
            return null;
        }

        return DeleteBookingServiceInput.builder()
                .bookingId(input.getBookingId())
                .updatedBy(updatedBy)
                .build();
    }

    public DeleteBookingResponse toResponse(DeleteBookingServiceOutput output) {
        DeleteBookingResponse response = new DeleteBookingResponse();
        response.setSuccess(true);
        return response;
    }
}
