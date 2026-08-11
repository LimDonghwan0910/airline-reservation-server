package airlineReservation.infra.mapper.customMapper;

import airlineReservation.domain.common.vo.GetFlightVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface GetFlightCustomMapper {
    List<GetFlightVo> selectFlightList(
            @Param("departureAirportId") String departureAirportId,
            @Param("arrivalAirportId") String arrivalAirportId,
            @Param("departureStartDatetime") LocalDateTime departureStartDatetime,
            @Param("departureEndDatetime") LocalDateTime departureEndDatetime,
            @Param("passengerCount") Integer passengerCount,
            @Param("cancelledStatus") String cancelledStatus,
            @Param("availableStatus") String availableStatus
    );
}
