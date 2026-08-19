package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.SearchAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.SearchAircraftServiceOutput;
import airlineReservation.infra.entity.Aircraft;
import airlineReservation.infra.entity.AircraftExample;
import airlineReservation.infra.mapper.AircraftMapper;
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
 * SearchAircraftService の単体テスト。
 * AircraftMapper をモックし、未削除条件・検索条件・一覧取得を検証する。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("航空機検索サービス")
class SearchAircraftServiceTest {

    @Mock
    private AircraftMapper aircraftMapper;

    @InjectMocks
    private SearchAircraftService searchAircraftService;

    @Nested
    @DisplayName("正常系")
    class Success {

        @Test
        @DisplayName("条件なしで検索すると未削除の航空機一覧を返す")
        void search_succeedsWithoutFilter() {
            // Given: 検索条件がなく、未削除の航空機が存在する
            SearchAircraftServiceInput input = SearchAircraftServiceInput.builder().build();
            Aircraft aircraft1 = activeAircraft("SEO123", "Boeing-777");
            Aircraft aircraft2 = activeAircraft("SEO456", "Airbus A320");

            when(aircraftMapper.selectByExample(any(AircraftExample.class)))
                    .thenReturn(List.of(aircraft1, aircraft2));

            // When: 航空機検索を実行する
            SearchAircraftServiceOutput output = searchAircraftService.search(input);

            // Then: 一覧が返却され、未削除条件・ID順で照会される
            assertThat(output.getAircraftList()).containsExactly(aircraft1, aircraft2);

            ArgumentCaptor<AircraftExample> exampleCaptor = ArgumentCaptor.forClass(AircraftExample.class);
            verify(aircraftMapper).selectByExample(exampleCaptor.capture());

            AircraftExample example = exampleCaptor.getValue();
            assertThat(example.getOrderByClause()).isEqualTo("aircraft_id ASC");
            assertThat(example.getOredCriteria()).hasSize(1);
            assertThat(example.getOredCriteria().get(0).getAllCriteria())
                    .anySatisfy(criterion -> {
                        assertThat(criterion.getCondition()).contains("is_deleted");
                        assertThat(criterion.getValue()).isEqualTo(false);
                    });
            assertThat(example.getOredCriteria().get(0).getAllCriteria())
                    .noneSatisfy(criterion -> assertThat(criterion.getCondition()).contains("aircraft_id"));
            assertThat(example.getOredCriteria().get(0).getAllCriteria())
                    .noneSatisfy(criterion -> assertThat(criterion.getCondition()).contains("aircraft_name"));
        }

        @Test
        @DisplayName("空白の検索条件は無視して未削除条件のみで照会する")
        void search_ignoresBlankFilters() {
            // Given: 航空機ID・機種名が空白のみ
            SearchAircraftServiceInput input = SearchAircraftServiceInput.builder()
                    .aircraftId(" ")
                    .aircraftName("")
                    .build();

            when(aircraftMapper.selectByExample(any(AircraftExample.class)))
                    .thenReturn(List.of());

            // When: 航空機検索を実行する
            searchAircraftService.search(input);

            // Then: 空白条件は付与されず、未削除条件のみで照会される
            ArgumentCaptor<AircraftExample> exampleCaptor = ArgumentCaptor.forClass(AircraftExample.class);
            verify(aircraftMapper).selectByExample(exampleCaptor.capture());

            AircraftExample example = exampleCaptor.getValue();
            assertThat(example.getOredCriteria().get(0).getAllCriteria()).hasSize(1);
            assertThat(example.getOredCriteria().get(0).getAllCriteria())
                    .anySatisfy(criterion -> {
                        assertThat(criterion.getCondition()).contains("is_deleted");
                        assertThat(criterion.getValue()).isEqualTo(false);
                    });
        }

        @Test
        @DisplayName("航空機IDで検索すると一致する一覧を返す")
        void search_succeedsWithAircraftId() {
            // Given: 航空機IDが指定され、一致する未削除航空機が存在する
            SearchAircraftServiceInput input = SearchAircraftServiceInput.builder()
                    .aircraftId("SEO123")
                    .build();
            Aircraft aircraft = activeAircraft("SEO123", "Boeing-777");

            when(aircraftMapper.selectByExample(any(AircraftExample.class)))
                    .thenReturn(List.of(aircraft));

            // When: 航空機検索を実行する
            SearchAircraftServiceOutput output = searchAircraftService.search(input);

            // Then: 一致した一覧が返却され、ID条件が付与される
            assertThat(output.getAircraftList()).containsExactly(aircraft);

            ArgumentCaptor<AircraftExample> exampleCaptor = ArgumentCaptor.forClass(AircraftExample.class);
            verify(aircraftMapper).selectByExample(exampleCaptor.capture());

            AircraftExample example = exampleCaptor.getValue();
            assertThat(example.getOredCriteria().get(0).getAllCriteria())
                    .anySatisfy(criterion -> {
                        assertThat(criterion.getCondition()).contains("aircraft_id");
                        assertThat(criterion.getValue()).isEqualTo("SEO123");
                    });
        }

        @Test
        @DisplayName("航空機名で検索すると部分一致の一覧を返す")
        void search_succeedsWithAircraftName() {
            // Given: 航空機名が指定され、部分一致する未削除航空機が存在する
            SearchAircraftServiceInput input = SearchAircraftServiceInput.builder()
                    .aircraftName("Boeing")
                    .build();
            Aircraft aircraft = activeAircraft("SEO123", "Boeing-777");

            when(aircraftMapper.selectByExample(any(AircraftExample.class)))
                    .thenReturn(List.of(aircraft));

            // When: 航空機検索を実行する
            SearchAircraftServiceOutput output = searchAircraftService.search(input);

            // Then: 部分一致条件で照会される
            assertThat(output.getAircraftList()).containsExactly(aircraft);

            ArgumentCaptor<AircraftExample> exampleCaptor = ArgumentCaptor.forClass(AircraftExample.class);
            verify(aircraftMapper).selectByExample(exampleCaptor.capture());

            AircraftExample example = exampleCaptor.getValue();
            assertThat(example.getOredCriteria().get(0).getAllCriteria())
                    .anySatisfy(criterion -> {
                        assertThat(criterion.getCondition()).contains("aircraft_name");
                        assertThat(criterion.getValue()).isEqualTo("%Boeing%");
                    });
        }

        @Test
        @DisplayName("条件に合致する航空機がない場合は空リストを返す")
        void search_succeedsWithEmptyList() {
            // Given: 条件に合致する航空機が存在しない
            SearchAircraftServiceInput input = SearchAircraftServiceInput.builder()
                    .aircraftId("UNKNOWN")
                    .build();

            when(aircraftMapper.selectByExample(any(AircraftExample.class)))
                    .thenReturn(List.of());

            // When: 航空機検索を実行する
            SearchAircraftServiceOutput output = searchAircraftService.search(input);

            // Then: 空リストが返却される
            assertThat(output.getAircraftList()).isEmpty();
            verify(aircraftMapper).selectByExample(any(AircraftExample.class));
        }
    }

    /** 未削除の航空機テストデータを返す。 */
    private Aircraft activeAircraft(String aircraftId, String aircraftName) {
        Aircraft aircraft = new Aircraft();
        aircraft.setAircraftId(aircraftId);
        aircraft.setAircraftName(aircraftName);
        aircraft.setRowCount(10);
        aircraft.setColumnCount(6);
        aircraft.setIsDeleted(false);
        return aircraft;
    }
}
