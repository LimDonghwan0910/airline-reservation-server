package airlineReservation.domain.booking.serviceInput;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 一般会員の予約検索サービス入力値。
 * Controller/Mapper で API リクエストを変換し、Service へ渡す。
 */
@Getter
@Builder
public class SearchBookingServiceInput {

    /** 検索対象の会員ID（本人の予約のみ検索） */
    private final Integer userId;

    /** 出発日フィルタ — null の場合は日付条件なしで全件検索 */
    private final LocalDate fromDate;

}
