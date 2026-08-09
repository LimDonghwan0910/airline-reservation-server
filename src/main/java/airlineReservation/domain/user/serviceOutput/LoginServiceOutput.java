package airlineReservation.domain.user.serviceOutput;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginServiceOutput {

    private Boolean success;
    private String accessToken;
}
