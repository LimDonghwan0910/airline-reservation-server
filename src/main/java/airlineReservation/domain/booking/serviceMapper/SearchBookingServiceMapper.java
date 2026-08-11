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
 * 一般会員の予約検索 DTO 変換。
 *
 * Controller（API DTO）↔ Service（ServiceInput/ServiceOutput）間の形式変換を担当する。
 * OpenAPI 自動生成 DTO とサービス内部モデルを分離するために存在する。
 */
@Component
public class SearchBookingServiceMapper {

    /** API リクエスト + userId → サービス入力値 */
    public SearchBookingServiceInput toServiceInput(Integer userId, SearchBookingRequest request) {
        return SearchBookingServiceInput.builder()
                .userId(userId)
                .fromDate(request != null ? request.getFromDate() : null)
                .build();
    }

    /** サービス結果 → API レスポンス JSON 形式 */
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

    /** 座席一覧を OpenAPI スキーマ（予約登録と同じ seat/name 構造）へ変換する */
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
