package airlineReservation.domain.booking.service;

import airlineReservation.domain.admin.serviceInput.SearchBookingByAdminServiceInput;
import airlineReservation.domain.booking.serviceInput.SearchBookingServiceInput;
import airlineReservation.domain.booking.serviceOutput.SearchBookingServiceOutput;
import airlineReservation.domain.booking.vo.SearchBookingVo;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.infra.mapper.customMapper.SearchBookingCustomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 予約検索サービス。
 *
 * DB検索結果は搭乗者（座席）数分だけ行が繰り返されるため（flat）、
 * buildOutput() で bookingId 単位にまとめて API レスポンス形式（nested）へ変換する。
 *
 * 一般会員（searchByMember）と管理者（searchByAdmin）はいずれも同じ buildOutput() を使用する。
 */
@Service
@RequiredArgsConstructor
public class SearchBookingService {

    private final SearchBookingCustomMapper searchBookingCustomMapper;

    /**
     * 一般会員の予約検索。
     * - 本人の userId の予約のみ検索
     * - fromDate がある場合、その日付以降の出発予約のみ検索
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
     * 管理者の予約検索。
     * - userId、aircraftId、出発日、到着日の条件はすべて任意（未指定ならフィルタしない）
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

    /** 一般会員検索時、userId は必須 */
    private void validateMemberInput(SearchBookingServiceInput input) {
        if (input.getUserId() == null) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "会員IDを入力してください。");
        }
    }

    /**
     * DB の flat 結果 → 予約単位のグループ変換。
     *
     * 例) 予約1件に搭乗者3名 → DB row 3件
     *     → bookingList 1件 + seats 3件へ変換
     *
     * 1回目の for: bookingId ごとに予約基本情報 + 座席リストを収集
     * 2回目の for: 収集したデータで BookingItem を組み立て
     */
    private SearchBookingServiceOutput buildOutput(List<SearchBookingVo> rows) {
        // LinkedHashMap: DB ORDER BY の順序を保持
        Map<Integer, SearchBookingVo> bookingRowById = new LinkedHashMap<>();
        Map<Integer, List<SearchBookingServiceOutput.SeatItem>> seatsByBookingId = new LinkedHashMap<>();

        for (SearchBookingVo row : rows) {
            Integer bookingId = row.getBookingId();

            // bookingId が初めて登場したとき、予約基本情報 + 空の座席リストを作成
            if (!bookingRowById.containsKey(bookingId)) {
                bookingRowById.put(bookingId, row);
                seatsByBookingId.put(bookingId, new ArrayList<>());
            }

            // LEFT JOIN のため、搭乗者/座席が無い場合 seatName が null になり得る
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

    /** SearchBookingVo + 座席リスト → BookingItem 変換。passengerCount = 座席数 */
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

    /** DB row 1件から座席/搭乗者情報のみを抽出 */
    private SearchBookingServiceOutput.SeatItem toSeatItem(SearchBookingVo row) {
        return SearchBookingServiceOutput.SeatItem.builder()
                .seat(row.getSeatName())
                .name(row.getPassengerName())
                .build();
    }

}
