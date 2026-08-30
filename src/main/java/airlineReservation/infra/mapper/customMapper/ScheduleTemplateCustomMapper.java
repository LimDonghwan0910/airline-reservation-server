package airlineReservation.infra.mapper.customMapper;

import airlineReservation.infra.entity.ScheduleTemplates;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 定期運航テンプレートに紐づく参照データの確認用 MyBatis Mapper。
 */
@Mapper
public interface ScheduleTemplateCustomMapper {

    /**
     * テンプレート行をロックして取得する。
     *
     * @param templateId テンプレートID
     * @return 対象テンプレート。存在しない場合は null
     */
    ScheduleTemplates selectByIdForUpdate(@Param("templateId") Integer templateId);

    /**
     * 同一便・同一区間のテンプレート行をロックする。
     *
     * @param aircraftId 航空機ID
     * @param departureAirportId 出発空港ID
     * @param arrivalAirportId 到着空港ID
     * @return ロックしたテンプレートID一覧
     */
    List<Integer> lockByAircraftAndRoute(
            @Param("aircraftId") String aircraftId,
            @Param("departureAirportId") String departureAirportId,
            @Param("arrivalAirportId") String arrivalAirportId
    );

    /**
     * テンプレートに紐づくスケジュール行をロックする。
     *
     * @param templateId テンプレートID
     * @return ロックしたスケジュールID一覧
     */
    List<Integer> lockLinkedSchedules(@Param("templateId") Integer templateId);

    /**
     * テンプレートに紐づくスケジュールの有効予約件数を取得する。
     *
     * @param templateId テンプレートID
     * @param bookingCancelledStatus 除外する予約ステータス
     * @return 有効予約件数
     */
    long countActiveBookings(
            @Param("templateId") Integer templateId,
            @Param("bookingCancelledStatus") String bookingCancelledStatus
    );
}
