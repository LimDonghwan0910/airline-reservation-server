package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceOutput.GetAirportServiceOutput;
import airlineReservation.domain.entity.Airport;
import airlineReservation.domain.entity.AirportExample;
import airlineReservation.infra.mapper.AirportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAirportService {

    private final AirportMapper airportMapper;

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
