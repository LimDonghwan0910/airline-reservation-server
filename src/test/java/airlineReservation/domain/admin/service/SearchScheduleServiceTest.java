package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.SearchScheduleServiceInput;
import airlineReservation.domain.admin.serviceOutput.SearchScheduleServiceOutput;
import airlineReservation.global.constant.Const;
import airlineReservation.infra.entity.Schedule;
import airlineReservation.infra.entity.ScheduleExample;
import airlineReservation.infra.mapper.ScheduleMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * SearchScheduleService の単体テスト。
 * ScheduleMapper をモックし、検索条件・一覧取得を検証する。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("運航スケジュール検索サービス")
class SearchScheduleServiceTest {

    @Mock
    private ScheduleMapper scheduleMapper;

    @InjectMocks
    private SearchScheduleService searchScheduleService;

    @Nested
    @DisplayName("正常系")
    class Success {

        @Test
        @DisplayName("条件なしで検索すると一覧を返す")
        void search_succeedsWithoutFilter() {
            // Given: 検索条件がなく、運航スケジュールが存在する
            SearchScheduleServiceInput input = SearchScheduleServiceInput.builder().build();
            Schedule schedule1 = existingSchedule(10, "SEO123");
            Schedule schedule2 = existingSchedule(11, "SEO456");

            when(scheduleMapper.selectByExample(any(ScheduleExample.class)))
                    .thenReturn(List.of(schedule1, schedule2));

            // When: 運航スケジュール検索を実行する
            SearchScheduleServiceOutput output = searchScheduleService.search(input);

            // Then: 一覧が返却され、出発日時順で照会される
            assertThat(output.getScheduleList()).containsExactly(schedule1, schedule2);

            ArgumentCaptor<ScheduleExample> exampleCaptor = ArgumentCaptor.forClass(ScheduleExample.class);
            verify(scheduleMapper).selectByExample(exampleCaptor.capture());

            ScheduleExample example = exampleCaptor.getValue();
            assertThat(example.getOrderByClause()).isEqualTo("departure_datetime ASC");
            assertThat(example.getOredCriteria().get(0).getAllCriteria()).isEmpty();
        }

        @Test
        @DisplayName("空白の検索条件は無視して全件照会する")
        void search_ignoresBlankFilters() {
            // Given: 航空機ID・空港IDが空白のみ
            SearchScheduleServiceInput input = SearchScheduleServiceInput.builder()
                    .aircraftId(" ")
                    .departureAirportId("")
                    .arrivalAirportId(" ")
                    .build();

            when(scheduleMapper.selectByExample(any(ScheduleExample.class)))
                    .thenReturn(List.of());

            // When: 運航スケジュール検索を実行する
            searchScheduleService.search(input);

            // Then: 空白条件は付与されない
            ArgumentCaptor<ScheduleExample> exampleCaptor = ArgumentCaptor.forClass(ScheduleExample.class);
            verify(scheduleMapper).selectByExample(exampleCaptor.capture());

            ScheduleExample example = exampleCaptor.getValue();
            assertThat(example.getOredCriteria().get(0).getAllCriteria()).isEmpty();
        }

        @Test
        @DisplayName("航空機IDで検索すると一致する一覧を返す")
        void search_succeedsWithAircraftId() {
            // Given: 航空機IDが指定され、一致する運航スケジュールが存在する
            SearchScheduleServiceInput input = SearchScheduleServiceInput.builder()
                    .aircraftId("SEO123")
                    .build();
            Schedule schedule = existingSchedule(10, "SEO123");

            when(scheduleMapper.selectByExample(any(ScheduleExample.class)))
                    .thenReturn(List.of(schedule));

            // When: 運航スケジュール検索を実行する
            SearchScheduleServiceOutput output = searchScheduleService.search(input);

            // Then: 一致した一覧が返却され、ID条件が付与される
            assertThat(output.getScheduleList()).containsExactly(schedule);

            ArgumentCaptor<ScheduleExample> exampleCaptor = ArgumentCaptor.forClass(ScheduleExample.class);
            verify(scheduleMapper).selectByExample(exampleCaptor.capture());

            ScheduleExample example = exampleCaptor.getValue();
            assertThat(example.getOredCriteria().get(0).getAllCriteria())
                    .anySatisfy(criterion -> {
                        assertThat(criterion.getCondition()).contains("aircraft_id");
                        assertThat(criterion.getValue()).isEqualTo("SEO123");
                    });
        }

        @Test
        @DisplayName("出発空港と到着空港で検索すると一致する一覧を返す")
        void search_succeedsWithAirports() {
            // Given: 出発空港・到着空港が指定され、一致する運航スケジュールが存在する
            SearchScheduleServiceInput input = SearchScheduleServiceInput.builder()
                    .departureAirportId("ICN")
                    .arrivalAirportId("NRT")
                    .build();
            Schedule schedule = existingSchedule(10, "SEO123");

            when(scheduleMapper.selectByExample(any(ScheduleExample.class)))
                    .thenReturn(List.of(schedule));

            // When: 運航スケジュール検索を実行する
            SearchScheduleServiceOutput output = searchScheduleService.search(input);

            // Then: 空港条件が付与される
            assertThat(output.getScheduleList()).containsExactly(schedule);

            ArgumentCaptor<ScheduleExample> exampleCaptor = ArgumentCaptor.forClass(ScheduleExample.class);
            verify(scheduleMapper).selectByExample(exampleCaptor.capture());

            ScheduleExample example = exampleCaptor.getValue();
            assertThat(example.getOredCriteria().get(0).getAllCriteria())
                    .anySatisfy(criterion -> {
                        assertThat(criterion.getCondition()).contains("departure_airport_id");
                        assertThat(criterion.getValue()).isEqualTo("ICN");
                    });
            assertThat(example.getOredCriteria().get(0).getAllCriteria())
                    .anySatisfy(criterion -> {
                        assertThat(criterion.getCondition()).contains("arrival_airport_id");
                        assertThat(criterion.getValue()).isEqualTo("NRT");
                    });
        }

        @Test
        @DisplayName("出発日で検索するとその日の一覧を返す")
        void search_succeedsWithDepartureDate() {
            // Given: 出発日が指定され、一致する運航スケジュールが存在する
            SearchScheduleServiceInput input = SearchScheduleServiceInput.builder()
                    .departureDate(LocalDate.of(2026, 8, 20))
                    .build();
            Schedule schedule = existingSchedule(10, "SEO123");

            when(scheduleMapper.selectByExample(any(ScheduleExample.class)))
                    .thenReturn(List.of(schedule));

            // When: 運航スケジュール検索を実行する
            SearchScheduleServiceOutput output = searchScheduleService.search(input);

            // Then: 指定日の0時以上〜翌日0時未満で照会される
            assertThat(output.getScheduleList()).containsExactly(schedule);

            ArgumentCaptor<ScheduleExample> exampleCaptor = ArgumentCaptor.forClass(ScheduleExample.class);
            verify(scheduleMapper).selectByExample(exampleCaptor.capture());

            ScheduleExample example = exampleCaptor.getValue();
            assertThat(example.getOredCriteria().get(0).getAllCriteria())
                    .anySatisfy(criterion -> {
                        assertThat(criterion.getCondition()).contains("departure_datetime");
                        assertThat(criterion.getValue()).isEqualTo(LocalDateTime.of(2026, 8, 20, 0, 0));
                    });
            assertThat(example.getOredCriteria().get(0).getAllCriteria())
                    .anySatisfy(criterion -> {
                        assertThat(criterion.getCondition()).contains("departure_datetime");
                        assertThat(criterion.getValue()).isEqualTo(LocalDateTime.of(2026, 8, 21, 0, 0));
                    });
        }

        @Test
        @DisplayName("条件に合致するスケジュールがない場合は空リストを返す")
        void search_succeedsWithEmptyList() {
            // Given: 条件に合致する運航スケジュールが存在しない
            SearchScheduleServiceInput input = SearchScheduleServiceInput.builder()
                    .aircraftId("UNKNOWN")
                    .build();

            when(scheduleMapper.selectByExample(any(ScheduleExample.class)))
                    .thenReturn(List.of());

            // When: 運航スケジュール検索を実行する
            SearchScheduleServiceOutput output = searchScheduleService.search(input);

            // Then: 空リストが返却される
            assertThat(output.getScheduleList()).isEmpty();
            verify(scheduleMapper).selectByExample(any(ScheduleExample.class));
        }
    }

    /** 検索結果として返す運航スケジュールのテストデータを返す。 */
    private Schedule existingSchedule(Integer scheduleId, String aircraftId) {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(scheduleId);
        schedule.setAircraftId(aircraftId);
        schedule.setStatus(Const.SCHEDULE_STATUS.SCHEDULED);
        return schedule;
    }
}
