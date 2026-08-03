package airlineReservation.domain.booking.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 예약 조회 MyBatis 조회 결과 1행.
 *
 * passenger_details LEFT JOIN 때문에 탑승객 1명 = row 1개.
 * 예) 3명 예약 → 동일 bookingId로 row 3개 반환 → Service에서 묶어야 함.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchBookingVo {

    // --- 예약 + 항공편 + 회원 정보 (bookingId당 동일 값 반복) ---
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

    // --- 탑승객/좌석 정보 (row마다 다름, 없으면 null) ---
    private String passengerName;
    private String seatName;

}
