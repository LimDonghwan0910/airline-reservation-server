package airlineReservation.domain.admin.controller;

import airlineReservation.domain.admin.serviceInput.SearchBookingByAdminServiceInput;
import airlineReservation.domain.admin.serviceMapper.SearchBookingByAdminServiceMapper;
import airlineReservation.domain.booking.service.DeleteBookingService;
import airlineReservation.domain.booking.service.SearchBookingService;
import airlineReservation.domain.booking.serviceInput.DeleteBookingServiceInput;
import airlineReservation.domain.booking.serviceMapper.DeleteBookingServiceMapper;
import airlineReservation.domain.booking.serviceOutput.DeleteBookingServiceOutput;
import airlineReservation.domain.booking.serviceOutput.SearchBookingServiceOutput;
import airlineReservation.global.constant.ApiEndpoints;
import airlineReservation.infra.dto.DeleteBookingRequest;
import airlineReservation.infra.dto.DeleteBookingResponse;
import airlineReservation.infra.dto.SearchBookingByAdminRequest;
import airlineReservation.infra.dto.SearchBookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** 管理者予約 API（検索、取消） */
@RestController
@RequiredArgsConstructor
public class AdminBookingController {

    private final SearchBookingService searchBookingService;
    private final SearchBookingByAdminServiceMapper searchBookingByAdminMapper;
    private final DeleteBookingService deleteBookingService;
    private final DeleteBookingServiceMapper deleteBookingMapper;

    /**
     * 管理者向け条件で予約一覧を検索する。
     *
     * @param request 予約検索リクエスト情報
     * @return 予約一覧レスポンス
     */
    @GetMapping(ApiEndpoints.Admin.SEARCH_BOOKING_BY_ADMIN)
    public ResponseEntity<SearchBookingResponse> searchBookingByAdmin(
            @ModelAttribute SearchBookingByAdminRequest request
    ) {
        SearchBookingByAdminServiceInput serviceInput = searchBookingByAdminMapper.toServiceInput(request);
        SearchBookingServiceOutput serviceOutput = searchBookingService.searchByAdmin(serviceInput);
        SearchBookingResponse response = searchBookingByAdminMapper.toResponse(serviceOutput);

        return ResponseEntity.ok(response);
    }

    /**
     * 予約を取消する。
     *
     * @param request 予約取消リクエスト情報
     * @return 取消結果レスポンス
     */
    @PostMapping(ApiEndpoints.Admin.DELETE_BOOKING)
    public ResponseEntity<DeleteBookingResponse> deleteBooking(
            @RequestBody DeleteBookingRequest request
    ) {
        DeleteBookingServiceInput serviceInput = deleteBookingMapper.toServiceInput(request);
        DeleteBookingServiceOutput serviceOutput = deleteBookingService.delete(serviceInput);
        DeleteBookingResponse response = deleteBookingMapper.toResponse(serviceOutput);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
