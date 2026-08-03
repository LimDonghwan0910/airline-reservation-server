package airlineReservation.domain.booking.controller;

import airlineReservation.domain.booking.service.CreateBookingService;
import airlineReservation.domain.booking.service.DeleteBookingService;
import airlineReservation.domain.booking.service.SearchBookingService;
import airlineReservation.domain.booking.serviceInput.CreateBookingServiceInput;
import airlineReservation.domain.booking.serviceInput.DeleteBookingServiceInput;
import airlineReservation.domain.booking.serviceInput.SearchBookingServiceInput;
import airlineReservation.domain.booking.serviceMapper.CreateBookingServiceMapper;
import airlineReservation.domain.booking.serviceMapper.DeleteBookingServiceMapper;
import airlineReservation.domain.booking.serviceMapper.SearchBookingServiceMapper;
import airlineReservation.domain.booking.serviceOutput.CreateBookingServiceOutput;
import airlineReservation.domain.booking.serviceOutput.DeleteBookingServiceOutput;
import airlineReservation.domain.booking.serviceOutput.SearchBookingServiceOutput;
import airlineReservation.global.constant.ApiEndpoints;
import airlineReservation.infra.dto.CreateBookingRequest;
import airlineReservation.infra.dto.CreateBookingResponse;
import airlineReservation.infra.dto.DeleteBookingRequest;
import airlineReservation.infra.dto.DeleteBookingResponse;
import airlineReservation.infra.dto.SearchBookingRequest;
import airlineReservation.infra.dto.SearchBookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** 일반회원 예약 API (등록, 조회) */
@RestController
@RequiredArgsConstructor
public class BookingController {

    private final CreateBookingService createBookingService;
    private final CreateBookingServiceMapper createBookingMapper;
    private final SearchBookingService searchBookingService;
    private final SearchBookingServiceMapper searchBookingMapper;
    private final DeleteBookingService deleteBookingService;
    private final DeleteBookingServiceMapper deleteBookingMapper;

    @PostMapping(ApiEndpoints.Booking.CREATE_BOOKING)
    public ResponseEntity<CreateBookingResponse> createBooking(
            @RequestBody CreateBookingRequest request
    ) {
        CreateBookingServiceInput serviceInput = createBookingMapper.toServiceInput(request);
        CreateBookingServiceOutput serviceOutput = createBookingService.create(serviceInput);
        CreateBookingResponse response = createBookingMapper.toResponse(serviceOutput);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * GET /api/v1/searchBooking — 일반회원 예약 조회
     *
     * @param userId  조회 대상 회원 ID (현재는 쿼리 파라미터, 추후 세션으로 변경 예정)
     * @param request fromDate — 이 날짜 이후 출발 예약만 조회 (선택)
     *
     * 흐름: Request DTO → ServiceInput → Service → ServiceOutput → Response DTO
     */
    @GetMapping(ApiEndpoints.Booking.SEARCH_BOOKING)
    public ResponseEntity<SearchBookingResponse> searchBooking(
            @RequestParam("user_id") Integer userId,
            @ModelAttribute SearchBookingRequest request
    ) {
        SearchBookingServiceInput serviceInput = searchBookingMapper.toServiceInput(userId, request);
        SearchBookingServiceOutput serviceOutput = searchBookingService.searchByMember(serviceInput);
        SearchBookingResponse response = searchBookingMapper.toResponse(serviceOutput);

        return ResponseEntity.ok(response);
    }

    @PostMapping(ApiEndpoints.Booking.DELETE_BOOKING)
    public ResponseEntity<DeleteBookingResponse> deleteBooking(
            @RequestBody DeleteBookingRequest request,
            @RequestParam(value = "user_id", required = false) Integer userId
    ) {
        DeleteBookingServiceInput serviceInput = deleteBookingMapper.toServiceInput(request, userId);
        DeleteBookingServiceOutput serviceOutput = deleteBookingService.delete(serviceInput);
        DeleteBookingResponse response = deleteBookingMapper.toResponse(serviceOutput);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
