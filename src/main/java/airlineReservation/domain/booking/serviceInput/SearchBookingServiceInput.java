package airlineReservation.domain.booking.serviceInput;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 일반회원 예약 조회 서비스 입력값.
 * Controller/Mapper에서 API 요청을 변환해 Service에 전달한다.
 */
@Getter
@Builder
public class SearchBookingServiceInput {

    /** 조회 대상 회원 ID (본인 예약만 조회) */
    private final Integer userId;

    /** 출발일 필터 — null이면 날짜 조건 없이 전체 조회 */
    private final LocalDate fromDate;

}
