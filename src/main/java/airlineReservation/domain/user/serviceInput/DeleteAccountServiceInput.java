package airlineReservation.domain.user.serviceInput;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteAccountServiceInput {

    private Integer userId;
}
