package airlineReservation.domain.user.controller;

import airlineReservation.domain.user.service.CreateAccountService;
import airlineReservation.domain.user.service.DeleteAccountService;
import airlineReservation.domain.user.service.LoginService;
import airlineReservation.domain.user.serviceInput.CreateAccountServiceInput;
import airlineReservation.domain.user.serviceInput.DeleteAccountServiceInput;
import airlineReservation.domain.user.serviceInput.LoginServiceInput;
import airlineReservation.domain.user.serviceMapper.CreateAccountServiceMapper;
import airlineReservation.domain.user.serviceMapper.DeleteAccountServiceMapper;
import airlineReservation.domain.user.serviceMapper.LoginServiceMapper;
import airlineReservation.domain.user.serviceOutput.CreateAccountServiceOutput;
import airlineReservation.domain.user.serviceOutput.DeleteAccountServiceOutput;
import airlineReservation.domain.user.serviceOutput.LoginServiceOutput;
import airlineReservation.global.constant.ApiEndpoints;
import airlineReservation.global.security.CustomUserDetails;
import airlineReservation.infra.dto.CreateAccountRequest;
import airlineReservation.infra.dto.CreateAccountResponse;
import airlineReservation.infra.dto.DeleteAccountResponse;
import airlineReservation.infra.dto.LoginRequest;
import airlineReservation.infra.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** 会員 API（登録、ログイン、退会） */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final CreateAccountService createAccountService;
    private final CreateAccountServiceMapper createAccountServiceMapper;
    private final LoginService loginService;
    private final LoginServiceMapper loginServiceMapper;
    private final DeleteAccountService deleteAccountService;
    private final DeleteAccountServiceMapper deleteAccountServiceMapper;

    /**
     * 新規会員登録を行う。
     *
     * @param request 会員登録リクエスト情報
     * @return 登録結果レスポンス
     */
    @PostMapping(ApiEndpoints.Auth.CREATE_ACCOUNT)
    public ResponseEntity<CreateAccountResponse> createAccount(
            @RequestBody CreateAccountRequest request
    ) {
        CreateAccountServiceInput serviceInput = createAccountServiceMapper.toServiceInput(request);
        CreateAccountServiceOutput serviceOutput = createAccountService.create(serviceInput);
        CreateAccountResponse response = createAccountServiceMapper.toResponse(serviceOutput);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 会員ログイン処理を行う。
     *
     * @param request ログインリクエスト情報
     * @return ログイン結果およびトークン情報
     */
    @PostMapping(ApiEndpoints.Auth.LOGIN)
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        LoginServiceInput serviceInput = loginServiceMapper.toServiceInput(request);
        LoginServiceOutput serviceOutput = loginService.login(serviceInput);
        LoginResponse response = loginServiceMapper.toResponse(serviceOutput);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 会員退会処理を行う。
     *
     * @param currentUser 認証済み会員情報
     * @return 退会処理結果レスポンス
     */
    @PostMapping(ApiEndpoints.Auth.DELETE_ACCOUNT)
    public ResponseEntity<DeleteAccountResponse> deleteAccount(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        DeleteAccountServiceInput serviceInput = DeleteAccountServiceInput.builder()
                .userId(currentUser.getUserId())
                .build();
        DeleteAccountServiceOutput serviceOutput = deleteAccountService.delete(serviceInput);
        DeleteAccountResponse response = deleteAccountServiceMapper.toResponse(serviceOutput);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
