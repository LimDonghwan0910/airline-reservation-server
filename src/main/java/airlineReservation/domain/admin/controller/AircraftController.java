package airlineReservation.domain.admin.controller;

import airlineReservation.domain.admin.service.CreateAircraftService;
import airlineReservation.domain.admin.service.DeleteAircraftService;
import airlineReservation.domain.admin.service.SearchAircraftService;
import airlineReservation.domain.admin.service.UpdateAircraftService;
import airlineReservation.domain.admin.serviceInput.CreateAircraftServiceInput;
import airlineReservation.domain.admin.serviceInput.DeleteAircraftServiceInput;
import airlineReservation.domain.admin.serviceInput.SearchAircraftServiceInput;
import airlineReservation.domain.admin.serviceInput.UpdateAircraftServiceInput;
import airlineReservation.domain.admin.serviceMapper.CreateAircraftServiceMapper;
import airlineReservation.domain.admin.serviceMapper.DeleteAircraftServiceMapper;
import airlineReservation.domain.admin.serviceMapper.SearchAircraftServiceMapper;
import airlineReservation.domain.admin.serviceMapper.UpdateAircraftServiceMapper;
import airlineReservation.domain.admin.serviceOutput.CreateAircraftServiceOutput;
import airlineReservation.domain.admin.serviceOutput.DeleteAircraftServiceOutput;
import airlineReservation.domain.admin.serviceOutput.SearchAircraftServiceOutput;
import airlineReservation.domain.admin.serviceOutput.UpdateAircraftServiceOutput;
import airlineReservation.global.constant.ApiEndpoints;
import airlineReservation.infra.dto.CreateAircraftRequest;
import airlineReservation.infra.dto.CreateAircraftResponse;
import airlineReservation.infra.dto.DeleteAircraftRequest;
import airlineReservation.infra.dto.DeleteAircraftResponse;
import airlineReservation.infra.dto.SearchAircraftRequest;
import airlineReservation.infra.dto.SearchAircraftResponse;
import airlineReservation.infra.dto.UpdateAircraftRequest;
import airlineReservation.infra.dto.UpdateAircraftResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** 航空機 API（登録、検索、更新、削除） */
@RestController
@RequiredArgsConstructor
public class AircraftController {

    private final CreateAircraftService createAircraftService;
    private final CreateAircraftServiceMapper createAircraftMapper;
    private final SearchAircraftService searchAircraftService;
    private final SearchAircraftServiceMapper searchAircraftMapper;
    private final DeleteAircraftService deleteAircraftService;
    private final DeleteAircraftServiceMapper deleteAircraftMapper;
    private final UpdateAircraftService updateAircraftService;
    private final UpdateAircraftServiceMapper updateAircraftMapper;

    /**
     * 航空機を新規登録する。
     *
     * @param request 航空機登録リクエスト情報
     * @return 登録結果レスポンス
     */
    @PostMapping(ApiEndpoints.Admin.CREATE_AIRCRAFT)
    public ResponseEntity<CreateAircraftResponse> createAircraft(
            @RequestBody CreateAircraftRequest request) {

        CreateAircraftServiceInput serviceInput = createAircraftMapper.toServiceInput(request);

        CreateAircraftServiceOutput serviceOutput = createAircraftService.create(serviceInput);

        CreateAircraftResponse response = createAircraftMapper.toResponse(serviceOutput);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * 条件に合致する航空機一覧を取得する。
     *
     * @param request 航空機検索リクエスト情報
     * @return 航空機一覧レスポンス
     */
    @GetMapping(ApiEndpoints.Admin.SEARCH_AIRCRAFT)
    public ResponseEntity<SearchAircraftResponse> searchAircraft(
            @ModelAttribute SearchAircraftRequest request
    ) {
        SearchAircraftServiceInput serviceInput = searchAircraftMapper.toServiceInput(request);
        SearchAircraftServiceOutput serviceOutput = searchAircraftService.search(serviceInput);
        SearchAircraftResponse response = searchAircraftMapper.toResponse(serviceOutput);

        return ResponseEntity.ok(response);
    }

    /**
     * 航空機情報を更新する。
     *
     * @param request 航空機更新リクエスト情報
     * @return 更新結果レスポンス
     */
    @PostMapping(ApiEndpoints.Admin.UPDATE_AIRCRAFT)
    public ResponseEntity<UpdateAircraftResponse> updateAircraft(
            @RequestBody UpdateAircraftRequest request) {

        UpdateAircraftServiceInput serviceInput = updateAircraftMapper.toServiceInput(request);

        UpdateAircraftServiceOutput serviceOutput = updateAircraftService.update(serviceInput);

        UpdateAircraftResponse response = updateAircraftMapper.toResponse(serviceOutput);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * 航空機を論理削除する。
     *
     * @param request 航空機削除リクエスト情報
     * @return 削除結果レスポンス
     */
    @PostMapping(ApiEndpoints.Admin.DELETE_AIRCRAFT)
    public ResponseEntity<DeleteAircraftResponse> deleteAircraft(
            @RequestBody DeleteAircraftRequest request) {

        DeleteAircraftServiceInput serviceInput = deleteAircraftMapper.toServiceInput(request);

        DeleteAircraftServiceOutput serviceOutput = deleteAircraftService.delete(serviceInput);

        DeleteAircraftResponse response = deleteAircraftMapper.toResponse(serviceOutput);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
