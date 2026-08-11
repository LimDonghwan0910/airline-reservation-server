package airlineReservation.domain.admin.serviceInput;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 管理者の予約検索サービス入力値。
 * 全フィールド任意 — null の場合、SQL で該当条件を適用しない。
 */
@Getter
@Builder
public class SearchBookingByAdminServiceInput {

    /** 会員IDフィルタ（任意） */
    private final Integer userId;

    /** 航空機IDフィルタ（任意） */
    private final String aircraftId;

    /** 出発日フィルタ — 該当日付と完全一致（任意） */
    private final LocalDate departureDate;

    /** 到着日フィルタ — 該当日付と完全一致（任意） */
    private final LocalDate arrivalDate;

}
