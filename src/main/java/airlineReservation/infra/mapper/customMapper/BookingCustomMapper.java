package airlineReservation.infra.mapper.customMapper;

import airlineReservation.infra.entity.Booking;
import airlineReservation.infra.entity.ScheduleSeat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 予約作成・キャンセル時の排他制御用 MyBatis Mapper。
 */
@Mapper
public interface BookingCustomMapper {

    /**
     * 予約行をロックして取得する。
     *
     * @param bookingId 予約ID
     * @return 対象予約。存在しない場合は null
     */
    Booking selectByIdForUpdate(@Param("bookingId") Integer bookingId);

    /**
     * 指定便の指定座席を昇順でロックして取得する。
     *
     * 複数座席を常に同じ順序でロックし、複数座席を含む同時予約時のデッドロックを抑止する。
     */
    List<ScheduleSeat> selectScheduleSeatsForUpdate(
            @Param("scheduleId") Integer scheduleId,
            @Param("seatNames") List<String> seatNames
    );

    /**
     * 座席IDを昇順でロックする。
     *
     * 予約キャンセルと予約作成が同じ座席を同時に更新しないようにする。
     */
    List<ScheduleSeat> selectScheduleSeatsByIdsForUpdate(
            @Param("scheduledSeatNos") List<Integer> scheduledSeatNos
    );
}
