package airlineReservation.domain.common.service;

import airlineReservation.domain.common.serviceOutput.GetAirportServiceOutput;
import airlineReservation.infra.entity.Airport;
import airlineReservation.infra.entity.AirportExample;
import airlineReservation.infra.mapper.AirportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 空港一覧取得処理を行うサービス。
 */
@Service
@RequiredArgsConstructor
public class GetAirportService {

    private final AirportMapper airportMapper;

    /**
     * 有効な空港一覧を取得する。
     *
     * @return serviceOutput
     */
    public GetAirportServiceOutput getAirportList() {
        AirportExample example = new AirportExample();
        example.createCriteria().andIsDeletedEqualTo(false);
        example.setOrderByClause("airport_id ASC");

        List<Airport> airportList = airportMapper.selectByExample(example);

        return GetAirportServiceOutput.builder()
                .airportList(airportList)
                .build();
    }
}
