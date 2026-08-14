package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.SearchScheduleServiceInput;
import airlineReservation.domain.admin.serviceOutput.SearchScheduleServiceOutput;
import airlineReservation.infra.entity.Schedule;
import airlineReservation.infra.entity.ScheduleExample;
import airlineReservation.infra.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 運航スケジュール検索処理を行うサービス。
 */
@Service
@RequiredArgsConstructor
public class SearchScheduleService {

    private final ScheduleMapper scheduleMapper;

    /**
     * 条件に合致する運航スケジュール一覧を取得する。
     *
     * @param input
     * @return serviceOutput
     */
    public SearchScheduleServiceOutput search(SearchScheduleServiceInput input) {
        ScheduleExample example = new ScheduleExample();
        ScheduleExample.Criteria criteria = example.createCriteria();

        if (StringUtils.hasText(input.getAircraftId())) {
            criteria.andAircraftIdEqualTo(input.getAircraftId());
        }
        if (StringUtils.hasText(input.getDepartureAirportId())) {
            criteria.andDepartureAirportIdEqualTo(input.getDepartureAirportId());
        }
        if (StringUtils.hasText(input.getArrivalAirportId())) {
            criteria.andArrivalAirportIdEqualTo(input.getArrivalAirportId());
        }
        if (input.getDepartureDate() != null) {
            LocalDate departureDate = input.getDepartureDate();
            LocalDateTime startOfDay = departureDate.atStartOfDay();
            LocalDateTime startOfNextDay = departureDate.plusDays(1).atStartOfDay();
            criteria.andDepartureDatetimeGreaterThanOrEqualTo(startOfDay);
            criteria.andDepartureDatetimeLessThan(startOfNextDay);
        }

        example.setOrderByClause("departure_datetime ASC");

        List<Schedule> scheduleList = scheduleMapper.selectByExample(example);

        return SearchScheduleServiceOutput.builder()
                .scheduleList(scheduleList)
                .build();
    }
}
