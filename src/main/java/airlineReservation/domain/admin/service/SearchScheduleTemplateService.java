package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.SearchScheduleTemplateServiceInput;
import airlineReservation.domain.admin.serviceOutput.SearchScheduleTemplateServiceOutput;
import airlineReservation.infra.entity.ScheduleTemplates;
import airlineReservation.infra.entity.ScheduleTemplatesExample;
import airlineReservation.infra.mapper.ScheduleTemplatesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 定期運航テンプレート検索処理を行うサービス。
 */
@Service
@RequiredArgsConstructor
public class SearchScheduleTemplateService {

    private final ScheduleTemplatesMapper scheduleTemplatesMapper;

    /**
     * 条件に合致する定期運航テンプレート一覧を取得する。
     *
     * @param input
     * @return serviceOutput
     */
    public SearchScheduleTemplateServiceOutput search(SearchScheduleTemplateServiceInput input) {
        ScheduleTemplatesExample example = new ScheduleTemplatesExample();
        ScheduleTemplatesExample.Criteria criteria = example.createCriteria();

        if (StringUtils.hasText(input.getAircraftId())) {
            criteria.andAircraftIdEqualTo(input.getAircraftId());
        }

        example.setOrderByClause("template_id DESC");

        List<ScheduleTemplates> scheduleTemplateList = scheduleTemplatesMapper.selectByExample(example);

        return SearchScheduleTemplateServiceOutput.builder()
                .scheduleTemplateList(scheduleTemplateList)
                .build();
    }
}
