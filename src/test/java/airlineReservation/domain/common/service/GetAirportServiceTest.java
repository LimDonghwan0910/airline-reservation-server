package airlineReservation.domain.common.service;

import airlineReservation.domain.common.serviceOutput.GetAirportServiceOutput;
import airlineReservation.infra.entity.Airport;
import airlineReservation.infra.entity.AirportExample;
import airlineReservation.infra.mapper.AirportMapper;
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
 * GetAirportService の単体テスト。
 * AirportMapper をモックし、有効空港一覧取得の正常系を検証する。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("空港一覧取得サービス")
class GetAirportServiceTest {

    @Mock
    private AirportMapper airportMapper;

    @InjectMocks
    private GetAirportService getAirportService;

    @Nested
    @DisplayName("正常系")
    class Success {

        @Test
        @DisplayName("有効な空港が存在する場合は一覧を返す")
        void getAirportList_succeedsWhenAirportsExist() {
            // Given: 未削除の空港が存在する
            Airport haneda = activeAirport("HND", "羽田空港");
            Airport narita = activeAirport("NRT", "成田空港");

            when(airportMapper.selectByExample(any(AirportExample.class)))
                    .thenReturn(List.of(haneda, narita));

            // When: 空港一覧取得を実行する
            GetAirportServiceOutput output = getAirportService.getAirportList();

            // Then: 空港一覧が返却され、未削除条件で照会される
            assertThat(output.getAirportList()).containsExactly(haneda, narita);

            ArgumentCaptor<AirportExample> exampleCaptor = ArgumentCaptor.forClass(AirportExample.class);
            verify(airportMapper).selectByExample(exampleCaptor.capture());

            AirportExample example = exampleCaptor.getValue();
            assertThat(example.getOrderByClause()).isEqualTo("airport_id ASC");
            assertThat(example.getOredCriteria()).hasSize(1);
            assertThat(example.getOredCriteria().get(0).getAllCriteria())
                    .anySatisfy(criterion -> {
                        assertThat(criterion.getCondition()).contains("is_deleted");
                        assertThat(criterion.getValue()).isEqualTo(false);
                    });
        }

        @Test
        @DisplayName("空港が存在しない場合は空リストを返す")
        void getAirportList_succeedsWithEmptyList() {
            // Given: 取得対象の空港が存在しない
            when(airportMapper.selectByExample(any(AirportExample.class)))
                    .thenReturn(List.of());

            // When: 空港一覧取得を実行する
            GetAirportServiceOutput output = getAirportService.getAirportList();

            // Then: 空リストが返却される
            assertThat(output.getAirportList()).isEmpty();
            verify(airportMapper).selectByExample(any(AirportExample.class));
        }
    }

    /** 未削除の空港テストデータを返す。 */
    private Airport activeAirport(String airportId, String nameKo) {
        Airport airport = new Airport();
        airport.setAirportId(airportId);
        airport.setAirportNameKo(nameKo);
        airport.setIsDeleted(false);
        return airport;
    }
}
