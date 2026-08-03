package airlineReservation.infra.mapper.customMapper;

import airlineReservation.domain.booking.vo.SearchBookingVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 예약 조회 MyBatis Mapper.
 * SQL은 SearchBookingCustomMapper.xml에 정의.
 */
@Mapper
public interface SearchBookingCustomMapper {

    /**
     * 일반회원 예약 목록 조회.
     * @param userId   본인 예약만 (필수)
     * @param fromDate 출발일 이후 필터 (선택)
     */
    List<SearchBookingVo> selectBookingListForMember(
            @Param("userId") Integer userId,
            @Param("fromDate") LocalDate fromDate
    );

    /**
     * 관리자 예약 목록 조회.
     * 모든 파라미터 선택 — null이면 해당 조건 미적용
     */
    List<SearchBookingVo> selectBookingListForAdmin(
            @Param("userId") Integer userId,
            @Param("aircraftId") String aircraftId,
            @Param("departureDate") LocalDate departureDate,
            @Param("arrivalDate") LocalDate arrivalDate
    );

}
