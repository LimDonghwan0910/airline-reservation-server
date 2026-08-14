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

@Component
public class SearchBookingByAdminServiceMapper {

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
