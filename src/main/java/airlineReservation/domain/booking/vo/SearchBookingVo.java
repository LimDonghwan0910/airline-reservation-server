package airlineReservation.domain.booking.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 予約検索 MyBatis 検索結果の1行。
 *
 * passenger_details の LEFT JOIN により搭乗者1名 = row 1件。
 * 例) 3名予約 → 同一 bookingId で row 3件返却 → Service でまとめる必要がある。
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchBookingVo {

    // --- 予約 + 航空便 + 会員情報（bookingId ごとに同じ値が繰り返される） ---
    private Integer bookingId;
    private Integer userId;
    private Integer scheduleId;
    private String userName;
    private String aircraftId;
    private String departureAirportId;
    private String arrivalAirportId;
    private LocalDateTime departureDatetime;
    private LocalDateTime arrivalDatetime;
    private String status;
    private Integer totalPrice;
    private LocalDateTime bookedTime;

    // --- 搭乗者/座席情報（row ごとに異なる。無い場合は null） ---
    private String passengerName;
    private String seatName;

}
