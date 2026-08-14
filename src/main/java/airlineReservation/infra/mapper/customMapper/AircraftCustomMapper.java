package airlineReservation.infra.mapper.customMapper;

import airlineReservation.domain.admin.vo.AircraftDeletionConstraintVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 航空機に紐づく参照データの確認用 MyBatis Mapper。
 */
@Mapper
public interface AircraftCustomMapper {

    /**
     * 航空機の更新・削除可否判定用の参照件数を1回の SQL で取得する。
     *
     * @param aircraftId 航空機ID
     * @param bookingCancelledStatus 除外する予約ステータス
     * @param scheduleCancelledStatus 除外する運航ステータス
     * @return 予約・運航スケジュール・定期運航テンプレート件数
     */
    AircraftDeletionConstraintVo selectDeletionConstraints(
            @Param("aircraftId") String aircraftId,
            @Param("bookingCancelledStatus") String bookingCancelledStatus,
            @Param("scheduleCancelledStatus") String scheduleCancelledStatus
    );
}
