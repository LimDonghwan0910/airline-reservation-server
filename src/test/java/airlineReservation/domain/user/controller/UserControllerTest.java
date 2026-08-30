package airlineReservation.domain.user.controller;

import airlineReservation.domain.user.service.CreateAccountService;
import airlineReservation.domain.user.service.DeleteAccountService;
import airlineReservation.domain.user.service.LoginService;
import airlineReservation.domain.user.serviceInput.DeleteAccountServiceInput;
import airlineReservation.domain.user.serviceMapper.CreateAccountServiceMapper;
import airlineReservation.domain.user.serviceMapper.DeleteAccountServiceMapper;
import airlineReservation.domain.user.serviceMapper.LoginServiceMapper;
import airlineReservation.domain.user.serviceOutput.DeleteAccountServiceOutput;
import airlineReservation.global.security.CustomUserDetails;
import airlineReservation.infra.dto.DeleteAccountResponse;
import airlineReservation.infra.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private CreateAccountService createAccountService;
    @Mock
    private CreateAccountServiceMapper createAccountServiceMapper;
    @Mock
    private LoginService loginService;
    @Mock
    private LoginServiceMapper loginServiceMapper;
    @Mock
    private DeleteAccountService deleteAccountService;
    @Mock
    private DeleteAccountServiceMapper deleteAccountServiceMapper;

    @InjectMocks
    private UserController userController;

    @Test
    void deleteAccount_usesAuthenticatedUsersId() {
        User user = new User();
        user.setUserId(42);
        CustomUserDetails currentUser = new CustomUserDetails(user);
        DeleteAccountResponse response = new DeleteAccountResponse();
        response.setSuccess(true);

        when(deleteAccountService.delete(any())).thenReturn(DeleteAccountServiceOutput.builder().build());
        when(deleteAccountServiceMapper.toResponse(any())).thenReturn(response);

        assertThat(userController.deleteAccount(currentUser).getBody()).isSameAs(response);

        ArgumentCaptor<DeleteAccountServiceInput> inputCaptor =
                ArgumentCaptor.forClass(DeleteAccountServiceInput.class);
        verify(deleteAccountService).delete(inputCaptor.capture());
        assertThat(inputCaptor.getValue().getUserId()).isEqualTo(42);
    }
}
