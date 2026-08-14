package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.SearchAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.SearchAircraftServiceOutput;
import airlineReservation.infra.entity.Aircraft;
import airlineReservation.infra.entity.AircraftExample;
import airlineReservation.infra.mapper.AircraftMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 航空機検索処理を行うサービス。
 */
@Service
@RequiredArgsConstructor
public class SearchAircraftService {

    private final AircraftMapper aircraftMapper;

    /**
     * 条件に合致する航空機一覧を取得する。
     *
     * @param input
     * @return serviceOutput
     */
    public SearchAircraftServiceOutput search(SearchAircraftServiceInput input) {

        AircraftExample example = new AircraftExample();
        AircraftExample.Criteria criteria = example.createCriteria();

        criteria.andIsDeletedEqualTo(false);

        if (StringUtils.hasText(input.getAircraftId())) {
            criteria.andAircraftIdEqualTo(input.getAircraftId());
        }

        if (StringUtils.hasText(input.getAircraftName())) {
            criteria.andAircraftNameLike("%" + input.getAircraftName() + "%");
        }

        example.setOrderByClause("aircraft_id ASC");

        List<Aircraft> aircraftList = aircraftMapper.selectByExample(example);

        return SearchAircraftServiceOutput.builder()
                .aircraftList(aircraftList)
                .build();
    }
}
