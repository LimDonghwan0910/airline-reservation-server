package airlineReservation.domain.booking.serviceInput;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteBookingServiceInput {

    private Integer bookingId;
    private Integer updatedBy;
}
