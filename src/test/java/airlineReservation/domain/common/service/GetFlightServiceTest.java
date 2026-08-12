package airlineReservation.domain.common.service;

import airlineReservation.domain.common.serviceInput.GetFlightServiceInput;
import airlineReservation.domain.common.serviceOutput.GetFlightServiceOutput;
import airlineReservation.domain.common.vo.GetFlightVo;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.entity.Airport;
import airlineReservation.infra.mapper.AirportMapper;
import airlineReservation.infra.mapper.customMapper.GetFlightCustomMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * GetFlightService の単体テスト。
 * GetFlightCustomMapper / AirportMapper をモックし、
 * 入力検証・空港存在確認・フライト一覧取得を検証する。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("フライト一覧取得サービス")
class GetFlightServiceTest {

    @Mock
    private GetFlightCustomMapper getFlightCustomMapper;

    @Mock
    private AirportMapper airportMapper;

    @InjectMocks
    private GetFlightService getFlightService;

    @Nested
    @DisplayName("正常系")
    class Success {

        @Test
        @DisplayName("有効な条件でフライトが存在する場合は一覧を返す")
        void getFlightList_succeedsWhenFlightsExist() {
            // Given: 空港が存在し、条件に合致するフライトがある
            GetFlightServiceInput input = validInput();
            LocalDate departureDate = input.getDepartureDate();
            GetFlightVo flight = sampleFlight();

            when(airportMapper.selectByPrimaryKey("HND")).thenReturn(activeAirport("HND"));
            when(airportMapper.selectByPrimaryKey("NRT")).thenReturn(activeAirport("NRT"));
            when(getFlightCustomMapper.selectFlightList(
                    eq("HND"),
                    eq("NRT"),
                    eq(departureDate.atStartOfDay()),
                    eq(departureDate.plusDays(1).atStartOfDay()),
                    eq(2),
                    eq(Const.BOOKING_STATUS.CANCELLED),
                    eq(Const.BOOKING_STATUS.AVAILABLE)
            )).thenReturn(List.of(flight));

            // When: フライト一覧取得を実行する
            GetFlightServiceOutput output = getFlightService.getFlightList(input);

            // Then: フライト一覧が返却される
            assertThat(output.getFlightList()).hasSize(1);
            assertThat(output.getFlightList().get(0).getScheduleId()).isEqualTo(100);
            assertThat(output.getFlightList().get(0).getAircraftId()).isEqualTo("NH001");
            assertThat(output.getFlightList().get(0).getDepartureAirportId()).isEqualTo("HND");
            assertThat(output.getFlightList().get(0).getArrivalAirportId()).isEqualTo("NRT");
            assertThat(output.getFlightList().get(0).getPrice()).isEqualTo(15000);
            assertThat(output.getFlightList().get(0).getAircraftName()).isEqualTo("Boeing 737");
            verify(getFlightCustomMapper).selectFlightList(
                    eq("HND"),
                    eq("NRT"),
                    eq(departureDate.atStartOfDay()),
                    eq(departureDate.plusDays(1).atStartOfDay()),
                    eq(2),
                    eq(Const.BOOKING_STATUS.CANCELLED),
                    eq(Const.BOOKING_STATUS.AVAILABLE)
            );
        }

        @Test
        @DisplayName("条件に合致するフライトがない場合は空リストを返す")
        void getFlightList_succeedsWithEmptyList() {
            // Given: 空港は存在するが、条件に合致するフライトがない
            GetFlightServiceInput input = validInput();
            LocalDate departureDate = input.getDepartureDate();

            when(airportMapper.selectByPrimaryKey("HND")).thenReturn(activeAirport("HND"));
            when(airportMapper.selectByPrimaryKey("NRT")).thenReturn(activeAirport("NRT"));
            when(getFlightCustomMapper.selectFlightList(
                    eq("HND"),
                    eq("NRT"),
                    eq(departureDate.atStartOfDay()),
                    eq(departureDate.plusDays(1).atStartOfDay()),
                    eq(2),
                    eq(Const.BOOKING_STATUS.CANCELLED),
                    eq(Const.BOOKING_STATUS.AVAILABLE)
            )).thenReturn(List.of());

            // When: フライト一覧取得を実行する
            GetFlightServiceOutput output = getFlightService.getFlightList(input);

            // Then: 空リストが返却される
            assertThat(output.getFlightList()).isEmpty();
        }
    }

    @Nested
    @DisplayName("例外系")
    class Failure {

        @Test
        @DisplayName("出発空港がない場合は例外が発生する")
        void getFlightList_failsWhenDepartureAirportIdIsBlank() {
            // Given: 出発空港が空白のみの入力
            GetFlightServiceInput input = GetFlightServiceInput.builder()
                    .departureAirportId(" ")
                    .arrivalAirportId("NRT")
                    .departureDate(LocalDate.now().plusDays(1))
                    .passengerCount(1)
                    .build();

            // When: フライト一覧取得を実行する
            // Then: 入力値例外が発生し、照会は行われない
            assertThatThrownBy(() -> getFlightService.getFlightList(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("出発空港を入力してください。");
            verify(airportMapper, never()).selectByPrimaryKey(anyString());
            verify(getFlightCustomMapper, never()).selectFlightList(
                    anyString(), anyString(), any(), any(), anyInt(), anyString(), anyString()
            );
        }

        @Test
        @DisplayName("到着空港がない場合は例外が発生する")
        void getFlightList_failsWhenArrivalAirportIdIsBlank() {
            // Given: 到着空港が空文字の入力
            GetFlightServiceInput input = GetFlightServiceInput.builder()
                    .departureAirportId("HND")
                    .arrivalAirportId("")
                    .departureDate(LocalDate.now().plusDays(1))
                    .passengerCount(1)
                    .build();

            // When: フライト一覧取得を実行する
            // Then: 入力値例外が発生し、照会は行われない
            assertThatThrownBy(() -> getFlightService.getFlightList(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("到着空港を入力してください。");
            verify(airportMapper, never()).selectByPrimaryKey(anyString());
            verify(getFlightCustomMapper, never()).selectFlightList(
                    anyString(), anyString(), any(), any(), anyInt(), anyString(), anyString()
            );
        }

        @Test
        @DisplayName("出発日がない場合は例外が発生する")
        void getFlightList_failsWhenDepartureDateIsNull() {
            // Given: 出発日が null の入力
            GetFlightServiceInput input = GetFlightServiceInput.builder()
                    .departureAirportId("HND")
                    .arrivalAirportId("NRT")
                    .departureDate(null)
                    .passengerCount(1)
                    .build();

            // When: フライト一覧取得を実行する
            // Then: 入力値例外が発生し、照会は行われない
            assertThatThrownBy(() -> getFlightService.getFlightList(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("出発日を入力してください。");
            verify(airportMapper, never()).selectByPrimaryKey(anyString());
            verify(getFlightCustomMapper, never()).selectFlightList(
                    anyString(), anyString(), any(), any(), anyInt(), anyString(), anyString()
            );
        }

        @Test
        @DisplayName("搭乗者数がない場合は例外が発生する")
        void getFlightList_failsWhenPassengerCountIsNull() {
            // Given: 搭乗者数が null の入力
            GetFlightServiceInput input = GetFlightServiceInput.builder()
                    .departureAirportId("HND")
                    .arrivalAirportId("NRT")
                    .departureDate(LocalDate.now().plusDays(1))
                    .passengerCount(null)
                    .build();

            // When: フライト一覧取得を実行する
            // Then: 入力値例外が発生し、照会は行われない
            assertThatThrownBy(() -> getFlightService.getFlightList(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("搭乗者数を入力してください。");
            verify(airportMapper, never()).selectByPrimaryKey(anyString());
            verify(getFlightCustomMapper, never()).selectFlightList(
                    anyString(), anyString(), any(), any(), anyInt(), anyString(), anyString()
            );
        }

        @Test
        @DisplayName("搭乗者数が1未満の場合は例外が発生する")
        void getFlightList_failsWhenPassengerCountIsNotPositive() {
            // Given: 搭乗者数が 0 の入力
            GetFlightServiceInput input = GetFlightServiceInput.builder()
                    .departureAirportId("HND")
                    .arrivalAirportId("NRT")
                    .departureDate(LocalDate.now().plusDays(1))
                    .passengerCount(0)
                    .build();

            // When: フライト一覧取得を実行する
            // Then: 入力値例外が発生し、照会は行われない
            assertThatThrownBy(() -> getFlightService.getFlightList(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("搭乗者数は1以上で入力してください。");
            verify(airportMapper, never()).selectByPrimaryKey(anyString());
            verify(getFlightCustomMapper, never()).selectFlightList(
                    anyString(), anyString(), any(), any(), anyInt(), anyString(), anyString()
            );
        }

        @Test
        @DisplayName("出発空港と到着空港が同じ場合は例外が発生する")
        void getFlightList_failsWhenAirportsAreSame() {
            // Given: 出発空港と到着空港が同一の入力
            GetFlightServiceInput input = GetFlightServiceInput.builder()
                    .departureAirportId("HND")
                    .arrivalAirportId("HND")
                    .departureDate(LocalDate.now().plusDays(1))
                    .passengerCount(1)
                    .build();

            // When: フライト一覧取得を実行する
            // Then: 入力値例外が発生し、照会は行われない
            assertThatThrownBy(() -> getFlightService.getFlightList(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("出発空港と到着空港は異なる空港を選択してください。");
            verify(airportMapper, never()).selectByPrimaryKey(anyString());
            verify(getFlightCustomMapper, never()).selectFlightList(
                    anyString(), anyString(), any(), any(), anyInt(), anyString(), anyString()
            );
        }

        @Test
        @DisplayName("出発日が過去の場合は例外が発生する")
        void getFlightList_failsWhenDepartureDateIsInPast() {
            // Given: 出発日が昨日の入力
            GetFlightServiceInput input = GetFlightServiceInput.builder()
                    .departureAirportId("HND")
                    .arrivalAirportId("NRT")
                    .departureDate(LocalDate.now().minusDays(1))
                    .passengerCount(1)
                    .build();

            // When: フライト一覧取得を実行する
            // Then: 入力値例外が発生し、照会は行われない
            assertThatThrownBy(() -> getFlightService.getFlightList(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("出発日は本日以降の日付を入力してください。");
            verify(airportMapper, never()).selectByPrimaryKey(anyString());
            verify(getFlightCustomMapper, never()).selectFlightList(
                    anyString(), anyString(), any(), any(), anyInt(), anyString(), anyString()
            );
        }

        @Test
        @DisplayName("出発空港が存在しない場合は例外が発生する")
        void getFlightList_failsWhenDepartureAirportDoesNotExist() {
            // Given: 出発空港が登録されていない
            GetFlightServiceInput input = validInput();

            when(airportMapper.selectByPrimaryKey("HND")).thenReturn(null);

            // When: フライト一覧取得を実行する
            // Then: NotFound 例外が発生し、フライト照会は行われない
            assertThatThrownBy(() -> getFlightService.getFlightList(input))
                    .isInstanceOfSatisfying(NotFoundException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo("登録されていない出発空港です: HND");
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.AIRPORT_NOT_FOUND);
                    });
            verify(getFlightCustomMapper, never()).selectFlightList(
                    anyString(), anyString(), any(), any(), anyInt(), anyString(), anyString()
            );
        }

        @Test
        @DisplayName("到着空港が削除済みの場合は例外が発生する")
        void getFlightList_failsWhenArrivalAirportIsDeleted() {
            // Given: 到着空港が論理削除済み
            GetFlightServiceInput input = validInput();

            Airport deletedArrival = activeAirport("NRT");
            deletedArrival.setIsDeleted(true);

            when(airportMapper.selectByPrimaryKey("HND")).thenReturn(activeAirport("HND"));
            when(airportMapper.selectByPrimaryKey("NRT")).thenReturn(deletedArrival);

            // When: フライト一覧取得を実行する
            // Then: NotFound 例外が発生し、フライト照会は行われない
            assertThatThrownBy(() -> getFlightService.getFlightList(input))
                    .isInstanceOfSatisfying(NotFoundException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo("登録されていない到着空港です: NRT");
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.AIRPORT_NOT_FOUND);
                    });
            verify(getFlightCustomMapper, never()).selectFlightList(
                    anyString(), anyString(), any(), any(), anyInt(), anyString(), anyString()
            );
        }
    }

    /** 正常系で共通利用する有効なフライト検索入力を返す。 */
    private GetFlightServiceInput validInput() {
        return GetFlightServiceInput.builder()
                .departureAirportId("HND")
                .arrivalAirportId("NRT")
                .departureDate(LocalDate.now().plusDays(1))
                .passengerCount(2)
                .build();
    }

    /** 未削除の空港テストデータを返す。 */
    private Airport activeAirport(String airportId) {
        Airport airport = new Airport();
        airport.setAirportId(airportId);
        airport.setIsDeleted(false);
        return airport;
    }

    /** 正常系で利用するフライトVOのテストデータを返す。 */
    private GetFlightVo sampleFlight() {
        return GetFlightVo.builder()
                .scheduleId(100)
                .aircraftId("NH001")
                .departureAirportId("HND")
                .arrivalAirportId("NRT")
                .departureDatetime(LocalDateTime.of(2026, 8, 12, 10, 0))
                .arrivalDatetime(LocalDateTime.of(2026, 8, 12, 11, 30))
                .price(15000)
                .aircraftName("Boeing 737")
                .build();
    }
}
