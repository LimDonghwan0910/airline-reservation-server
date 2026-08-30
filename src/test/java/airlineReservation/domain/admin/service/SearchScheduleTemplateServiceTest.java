package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.SearchScheduleTemplateServiceInput;
import airlineReservation.domain.admin.serviceOutput.SearchScheduleTemplateServiceOutput;
import airlineReservation.infra.entity.ScheduleTemplates;
import airlineReservation.infra.entity.ScheduleTemplatesExample;
import airlineReservation.infra.mapper.ScheduleTemplatesMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * SearchScheduleTemplateService の単体テスト。
 * ScheduleTemplatesMapper をモックし、検索条件・一覧取得を検証する。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("定期運航テンプレート検索サービス")
class SearchScheduleTemplateServiceTest {

    @Mock
    private ScheduleTemplatesMapper scheduleTemplatesMapper;

    @InjectMocks
    private SearchScheduleTemplateService searchScheduleTemplateService;

    @Nested
    @DisplayName("正常系")
    class Success {

        @Test
        @DisplayName("条件なしで検索すると一覧を返す")
        void search_succeedsWithoutFilter() {
            // Given: 検索条件がなく、定期運航テンプレートが存在する
            SearchScheduleTemplateServiceInput input = SearchScheduleTemplateServiceInput.builder().build();
            ScheduleTemplates template1 = existingTemplate(2, "SEO456");
            ScheduleTemplates template2 = existingTemplate(1, "SEO123");

            when(scheduleTemplatesMapper.selectByExample(any(ScheduleTemplatesExample.class)))
                    .thenReturn(List.of(template1, template2));

            // When: 定期運航テンプレート検索を実行する
            SearchScheduleTemplateServiceOutput output = searchScheduleTemplateService.search(input);

            // Then: 一覧が返却され、テンプレートID降順で照会される
            assertThat(output.getScheduleTemplateList()).containsExactly(template1, template2);

            ArgumentCaptor<ScheduleTemplatesExample> exampleCaptor =
                    ArgumentCaptor.forClass(ScheduleTemplatesExample.class);
            verify(scheduleTemplatesMapper).selectByExample(exampleCaptor.capture());

            ScheduleTemplatesExample example = exampleCaptor.getValue();
            assertThat(example.getOrderByClause()).isEqualTo("template_id DESC");
            assertThat(example.getOredCriteria().get(0).getAllCriteria()).isEmpty();
        }

        @Test
        @DisplayName("空白の航空機IDは無視して全件照会する")
        void search_ignoresBlankAircraftId() {
            // Given: 航空機IDが空白のみ
            SearchScheduleTemplateServiceInput input = SearchScheduleTemplateServiceInput.builder()
                    .aircraftId(" ")
                    .build();

            when(scheduleTemplatesMapper.selectByExample(any(ScheduleTemplatesExample.class)))
                    .thenReturn(List.of());

            // When: 定期運航テンプレート検索を実行する
            searchScheduleTemplateService.search(input);

            // Then: 空白条件は付与されない
            ArgumentCaptor<ScheduleTemplatesExample> exampleCaptor =
                    ArgumentCaptor.forClass(ScheduleTemplatesExample.class);
            verify(scheduleTemplatesMapper).selectByExample(exampleCaptor.capture());

            ScheduleTemplatesExample example = exampleCaptor.getValue();
            assertThat(example.getOredCriteria().get(0).getAllCriteria()).isEmpty();
        }

        @Test
        @DisplayName("航空機IDで検索すると一致する一覧を返す")
        void search_succeedsWithAircraftId() {
            // Given: 航空機IDが指定され、一致する定期運航テンプレートが存在する
            SearchScheduleTemplateServiceInput input = SearchScheduleTemplateServiceInput.builder()
                    .aircraftId("SEO123")
                    .build();
            ScheduleTemplates template = existingTemplate(1, "SEO123");

            when(scheduleTemplatesMapper.selectByExample(any(ScheduleTemplatesExample.class)))
                    .thenReturn(List.of(template));

            // When: 定期運航テンプレート検索を実行する
            SearchScheduleTemplateServiceOutput output = searchScheduleTemplateService.search(input);

            // Then: 一致した一覧が返却され、ID条件が付与される
            assertThat(output.getScheduleTemplateList()).containsExactly(template);

            ArgumentCaptor<ScheduleTemplatesExample> exampleCaptor =
                    ArgumentCaptor.forClass(ScheduleTemplatesExample.class);
            verify(scheduleTemplatesMapper).selectByExample(exampleCaptor.capture());

            ScheduleTemplatesExample example = exampleCaptor.getValue();
            assertThat(example.getOredCriteria().get(0).getAllCriteria())
                    .anySatisfy(criterion -> {
                        assertThat(criterion.getCondition()).contains("aircraft_id");
                        assertThat(criterion.getValue()).isEqualTo("SEO123");
                    });
        }

        @Test
        @DisplayName("条件に合致するテンプレートがない場合は空リストを返す")
        void search_succeedsWithEmptyList() {
            // Given: 条件に合致する定期運航テンプレートが存在しない
            SearchScheduleTemplateServiceInput input = SearchScheduleTemplateServiceInput.builder()
                    .aircraftId("UNKNOWN")
                    .build();

            when(scheduleTemplatesMapper.selectByExample(any(ScheduleTemplatesExample.class)))
                    .thenReturn(List.of());

            // When: 定期運航テンプレート検索を実行する
            SearchScheduleTemplateServiceOutput output = searchScheduleTemplateService.search(input);

            // Then: 空リストが返却される
            assertThat(output.getScheduleTemplateList()).isEmpty();
            verify(scheduleTemplatesMapper).selectByExample(any(ScheduleTemplatesExample.class));
        }
    }

    /** 検索結果として返す定期運航テンプレートのテストデータを返す。 */
    private ScheduleTemplates existingTemplate(Integer templateId, String aircraftId) {
        ScheduleTemplates template = new ScheduleTemplates();
        template.setTemplateId(templateId);
        template.setAircraftId(aircraftId);
        return template;
    }
}
