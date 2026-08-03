package airlineReservation.domain.booking.serviceMapper;

import airlineReservation.domain.booking.serviceInput.SearchBookingServiceInput;
import airlineReservation.domain.booking.serviceOutput.SearchBookingServiceOutput;
import airlineReservation.infra.dto.CreateBookingRequestPassengerListInner;
import airlineReservation.infra.dto.SearchBookingRequest;
import airlineReservation.infra.dto.SearchBookingResponse;
import airlineReservation.infra.dto.SearchBookingResponseBookingListInner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 일반회원 예약 조회 DTO 변환.
 *
 * Controller(API DTO) ↔ Service(ServiceInput/ServiceOutput) 사이 형식 변환 담당.
 * OpenAPI 자동생성 DTO와 서비스 내부 모델을 분리하기 위해 존재한다.
 */
@Component
public class SearchBookingServiceMapper {

    /** API 요청 + userId → 서비스 입력값 */
    public SearchBookingServiceInput toServiceInput(Integer userId, SearchBookingRequest request) {
        return SearchBookingServiceInput.builder()
                .userId(userId)
                .fromDate(request != null ? request.getFromDate() : null)
                .build();
    }

    /** 서비스 결과 → API 응답 JSON 형태 */
    public SearchBookingResponse toResponse(SearchBookingServiceOutput output) {
        SearchBookingResponse response = new SearchBookingResponse();
        List<SearchBookingResponseBookingListInner> bookingList = new ArrayList<>();

        if (output.getBookingList() != null) {
            for (SearchBookingServiceOutput.BookingItem item : output.getBookingList()) {
                SearchBookingResponseBookingListInner apiItem = new SearchBookingResponseBookingListInner();
                apiItem.setBookingId(item.getBookingId());
                apiItem.setUserId(item.getUserId());
                apiItem.setScheduleId(item.getScheduleId());
                apiItem.setUserName(item.getUserName());
                apiItem.setAircraftId(item.getAircraftId());
                apiItem.setDepartureAirportId(item.getDepartureAirportId());
                apiItem.setArrivalAirportId(item.getArrivalAirportId());
                apiItem.setDepartureDatetime(item.getDepartureDatetime());
                apiItem.setArrivalDatetime(item.getArrivalDatetime());
                apiItem.setStatus(item.getStatus());
                apiItem.setTotalPrice(item.getTotalPrice());
                apiItem.setPassengerCount(item.getPassengerCount());
                apiItem.setBookedTime(item.getBookedTime());
                apiItem.setSeats(toSeatList(item.getSeats()));
                bookingList.add(apiItem);
            }
        }

        response.setBookingList(bookingList);
        return response;
    }

    /** 좌석 목록을 OpenAPI 스키마(예약등록과 동일한 seat/name 구조)로 변환 */
    private List<CreateBookingRequestPassengerListInner> toSeatList(
            List<SearchBookingServiceOutput.SeatItem> seats
    ) {
        List<CreateBookingRequestPassengerListInner> seatList = new ArrayList<>();
        if (seats == null) {
            return seatList;
        }

        for (SearchBookingServiceOutput.SeatItem seat : seats) {
            CreateBookingRequestPassengerListInner apiSeat = new CreateBookingRequestPassengerListInner();
            apiSeat.setSeat(seat.getSeat());
            apiSeat.setName(seat.getName());
            seatList.add(apiSeat);
        }

        return seatList;
    }

}
