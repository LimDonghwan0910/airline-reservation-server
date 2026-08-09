package airlineReservation.domain.user.serviceInput;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CreateAccountServiceInput {

    private String userName;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDate birthDate;
}
