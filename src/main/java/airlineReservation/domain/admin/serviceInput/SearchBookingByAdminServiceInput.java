package airlineReservation.domain.admin.serviceInput;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 관리자 예약 조회 서비스 입력값.
 * 모든 필드 선택 — null이면 SQL에서 해당 조건을 적용하지 않음.
 */
@Getter
@Builder
public class SearchBookingByAdminServiceInput {

    /** 회원 ID 필터 (선택) */
    private final Integer userId;

    /** 항공기 ID 필터 (선택) */
    private final String aircraftId;

    /** 출발일 필터 — 해당 날짜와 정확히 일치 (선택) */
    private final LocalDate departureDate;

    /** 도착일 필터 — 해당 날짜와 정확히 일치 (선택) */
    private final LocalDate arrivalDate;

}
