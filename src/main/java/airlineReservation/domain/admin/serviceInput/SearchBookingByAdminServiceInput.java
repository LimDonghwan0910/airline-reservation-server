package airlineReservation.domain.admin.serviceInput;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class SearchBookingByAdminServiceInput {

    private final Integer userId;

    private final String aircraftId;

    private final LocalDate departureDate;

    private final LocalDate arrivalDate;

}
