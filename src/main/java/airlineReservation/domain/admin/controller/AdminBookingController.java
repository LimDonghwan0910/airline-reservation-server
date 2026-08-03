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

/** 관리자 예약 조회 API */
@RestController
@RequiredArgsConstructor
public class AdminBookingController {

    private final SearchBookingService searchBookingService;
    private final SearchBookingByAdminServiceMapper searchBookingByAdminMapper;
    private final DeleteBookingService deleteBookingService;
    private final DeleteBookingServiceMapper deleteBookingMapper;

    /**
     * GET /api/v1/admin/searchBooking — 관리자 예약 조회
     *
     * 일반회원 조회와 동일한 SearchBookingService.searchByAdmin() + buildOutput() 사용.
     * 차이는 검색 조건(userId, aircraftId, 출발일, 도착일)뿐.
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
