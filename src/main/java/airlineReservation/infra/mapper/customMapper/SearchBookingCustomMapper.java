package airlineReservation.infra.mapper.customMapper;

import airlineReservation.domain.booking.vo.SearchBookingVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 予約検索 MyBatis Mapper。
 * SQL は SearchBookingCustomMapper.xml に定義する。
 */
@Mapper
public interface SearchBookingCustomMapper {

    /**
     * 一般会員の予約一覧検索。
     * @param userId   本人の予約のみ（必須）
     * @param fromDate 出発日以降フィルタ（任意）
     */
    List<SearchBookingVo> selectBookingListForMember(
            @Param("userId") Integer userId,
            @Param("fromDate") LocalDate fromDate
    );

    /**
     * 管理者の予約一覧検索。
     * すべてのパラメータは任意 — null の場合、該当条件は適用しない
     */
    List<SearchBookingVo> selectBookingListForAdmin(
            @Param("userId") Integer userId,
            @Param("aircraftId") String aircraftId,
            @Param("departureDate") LocalDate departureDate,
            @Param("arrivalDate") LocalDate arrivalDate
    );

}
