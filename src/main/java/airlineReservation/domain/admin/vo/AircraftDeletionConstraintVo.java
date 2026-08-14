package airlineReservation.domain.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 航空機の更新・削除可否判定用の参照件数。
 * 予約・運航スケジュール・定期運航テンプレート件数を取得する。
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AircraftDeletionConstraintVo {

    private long activeBookingCount;
    private long activeScheduleCount;
    private long scheduleTemplateCount;
}
