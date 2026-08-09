package airlineReservation.domain.user.serviceInput;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginServiceInput {

    private String email;
    private String password;
}
