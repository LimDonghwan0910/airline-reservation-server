package airlineReservation.domain.booking.service;

import airlineReservation.domain.admin.serviceInput.SearchBookingByAdminServiceInput;
import airlineReservation.domain.booking.serviceInput.SearchBookingServiceInput;
import airlineReservation.domain.booking.serviceOutput.SearchBookingServiceOutput;
import airlineReservation.domain.booking.vo.SearchBookingVo;
import airlineReservation.infra.mapper.customMapper.SearchBookingCustomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 예약 조회 서비스.
 *
 * DB 조회 결과는 탑승객(좌석) 수만큼 row가 반복되므로(flat),
 * buildOutput()에서 bookingId 기준으로 묶어 API 응답 형태(nested)로 변환한다.
 *
 * 일반회원(searchByMember)과 관리자(searchByAdmin) 모두 같은 buildOutput()을 사용한다.
 */
@Service
@RequiredArgsConstructor
public class SearchBookingService {

    private final SearchBookingCustomMapper searchBookingCustomMapper;

    /**
     * 일반회원 예약 조회.
     * - 본인 userId의 예약만 조회
     * - fromDate가 있으면 해당 날짜 이후 출발 예약만 조회
     */
    public SearchBookingServiceOutput searchByMember(SearchBookingServiceInput input) {
        validateMemberInput(input);

        List<SearchBookingVo> rows = searchBookingCustomMapper.selectBookingListForMember(
                input.getUserId(),
                input.getFromDate()
        );

        return buildOutput(rows);
    }

    /**
     * 관리자 예약 조회.
     * - userId, aircraftId, 출발일, 도착일 조건은 모두 선택(없으면 필터 안 함)
     */
    public SearchBookingServiceOutput searchByAdmin(SearchBookingByAdminServiceInput input) {
        List<SearchBookingVo> rows = searchBookingCustomMapper.selectBookingListForAdmin(
                input.getUserId(),
                input.getAircraftId(),
                input.getDepartureDate(),
                input.getArrivalDate()
        );

        return buildOutput(rows);
    }

    /** 일반회원 조회 시 userId는 필수 */
    private void validateMemberInput(SearchBookingServiceInput input) {
        if (input.getUserId() == null) {
            throw new IllegalArgumentException("회원 ID를 입력해 주세요.");
        }
    }

    /**
     * DB flat 결과 → 예약별 그룹 변환.
     *
     * 예) 예약 1건에 3명 탑승 → DB row 3개
     *     → bookingList 1건 + seats 3개로 변환
     *
     * 1차 for: bookingId별로 예약 기본정보 + 좌석 리스트 수집
     * 2차 for: 수집한 데이터로 BookingItem 완성
     */
    private SearchBookingServiceOutput buildOutput(List<SearchBookingVo> rows) {
        // LinkedHashMap: DB ORDER BY 순서 유지
        Map<Integer, SearchBookingVo> bookingRowById = new LinkedHashMap<>();
        Map<Integer, List<SearchBookingServiceOutput.SeatItem>> seatsByBookingId = new LinkedHashMap<>();

        for (SearchBookingVo row : rows) {
            Integer bookingId = row.getBookingId();

            // bookingId 처음 등장 시 예약 기본정보 + 빈 좌석 리스트 생성
            if (!bookingRowById.containsKey(bookingId)) {
                bookingRowById.put(bookingId, row);
                seatsByBookingId.put(bookingId, new ArrayList<>());
            }

            // LEFT JOIN이라 탑승객/좌석 없으면 seatName이 null일 수 있음
            if (row.getSeatName() != null) {
                seatsByBookingId.get(bookingId).add(toSeatItem(row));
            }
        }

        List<SearchBookingServiceOutput.BookingItem> bookingList = new ArrayList<>();
        for (Map.Entry<Integer, SearchBookingVo> entry : bookingRowById.entrySet()) {
            Integer bookingId = entry.getKey();
            List<SearchBookingServiceOutput.SeatItem> seats = seatsByBookingId.get(bookingId);
            bookingList.add(toBookingItem(entry.getValue(), seats));
        }

        return SearchBookingServiceOutput.builder()
                .bookingList(bookingList)
                .build();
    }

    /** SearchBookingVo + 좌석 리스트 → BookingItem 변환. passengerCount = 좌석 수 */
    private SearchBookingServiceOutput.BookingItem toBookingItem(
            SearchBookingVo row,
            List<SearchBookingServiceOutput.SeatItem> seats
    ) {
        return SearchBookingServiceOutput.BookingItem.builder()
                .bookingId(row.getBookingId())
                .userId(row.getUserId())
                .scheduleId(row.getScheduleId())
                .userName(row.getUserName())
                .aircraftId(row.getAircraftId())
                .departureAirportId(row.getDepartureAirportId())
                .arrivalAirportId(row.getArrivalAirportId())
                .departureDatetime(row.getDepartureDatetime())
                .arrivalDatetime(row.getArrivalDatetime())
                .status(row.getStatus())
                .totalPrice(row.getTotalPrice())
                .passengerCount(seats.size())
                .seats(seats)
                .bookedTime(row.getBookedTime())
                .build();
    }

    /** DB row 1건에서 좌석/탑승객 정보만 추출 */
    private SearchBookingServiceOutput.SeatItem toSeatItem(SearchBookingVo row) {
        return SearchBookingServiceOutput.SeatItem.builder()
                .seat(row.getSeatName())
                .name(row.getPassengerName())
                .build();
    }

}
