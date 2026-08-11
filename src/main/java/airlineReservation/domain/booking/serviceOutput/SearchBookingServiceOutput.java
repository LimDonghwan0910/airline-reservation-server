package airlineReservation.domain.booking.serviceOutput;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 予約検索サービスの結果。
 *
 * DB では座席ごとに row が出るが、Service の buildOutput() で
 * 予約1件 = BookingItem + seats[] の形にまとめられる。
 */
@Getter
@Builder
public class SearchBookingServiceOutput {

    private final List<BookingItem> bookingList;

    /** 予約1件（航空便情報 + 搭乗者/座席一覧） */
    @Getter
    @Builder
    public static class BookingItem {

        private final Integer bookingId;
        private final Integer userId;
        private final Integer scheduleId;
        private final String userName;
        private final String aircraftId;
        private final String departureAirportId;
        private final String arrivalAirportId;
        private final LocalDateTime departureDatetime;
        private final LocalDateTime arrivalDatetime;
        private final String status;
        private final Integer totalPrice;
        /** 搭乗者数 = seats.size() */
        private final Integer passengerCount;
        private final List<SeatItem> seats;
        private final LocalDateTime bookedTime;

    }

    /** 搭乗者1名の座席情報 */
    @Getter
    @Builder
    public static class SeatItem {

        private final String seat;
        private final String name;

    }

}
