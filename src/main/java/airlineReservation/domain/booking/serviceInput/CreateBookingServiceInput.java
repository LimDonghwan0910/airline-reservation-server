package airlineReservation.domain.booking.serviceInput;
import airlineReservation.infra.dto.CreateBookingRequestPassengerListInner;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class CreateBookingServiceInput {

    private Integer userId;
    private Integer scheduleId;
    private Integer totalPrice;
    private List<CreateBookingRequestPassengerListInner> passengerList;

}