package airlineReservation.domain.admin.serviceMapper;

import airlineReservation.domain.admin.serviceInput.SearchBookingByAdminServiceInput;
import airlineReservation.domain.booking.serviceOutput.SearchBookingServiceOutput;
import airlineReservation.infra.dto.CreateBookingRequestPassengerListInner;
import airlineReservation.infra.dto.SearchBookingByAdminRequest;
import airlineReservation.infra.dto.SearchBookingResponse;
import airlineReservation.infra.dto.SearchBookingResponseBookingListInner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 관리자 예약 조회 DTO 변환.
 * SearchBookingServiceMapper와 toResponse 로직은 동일, 입력 조건만 다름.
 */
@Component
public class SearchBookingByAdminServiceMapper {

    /** API 요청 → 서비스 입력값 (aircraftId는 API에서 Integer → String 변환) */
    public SearchBookingByAdminServiceInput toServiceInput(SearchBookingByAdminRequest request) {
        if (request == null) {
            return SearchBookingByAdminServiceInput.builder().build();
        }

        String aircraftId = request.getAircraftId() != null
                ? String.valueOf(request.getAircraftId())
                : null;

        return SearchBookingByAdminServiceInput.builder()
                .userId(request.getUserId())
                .aircraftId(aircraftId)
                .departureDate(request.getDepartureDate())
                .arrivalDate(request.getArrivalDate())
                .build();
    }

    /** 서비스 결과 → API 응답 (일반회원 조회와 동일한 Response 구조) */
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
