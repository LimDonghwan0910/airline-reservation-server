package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.CreateAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.CreateAircraftServiceOutput;
import airlineReservation.domain.admin.validator.AircraftInputValidator;
import airlineReservation.global.exception.DuplicateException;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.infra.entity.Aircraft;
import airlineReservation.infra.mapper.AircraftMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * CreateAircraftService の単体テスト。
 * AircraftMapper をモックし、入力検証・重複チェック・登録処理を検証する。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("航空機登録サービス")
class CreateAircraftServiceTest {

    @Mock
    private AircraftMapper aircraftMapper;

    @Spy
    private AircraftInputValidator aircraftInputValidator = new AircraftInputValidator();

    @InjectMocks
    private CreateAircraftService createAircraftService;

    @Nested
    @DisplayName("正常系")
    class Success {

        @Test
        @DisplayName("有効な入力で航空機を登録すると成功する")
        void create_succeedsWithValidInput() {
            // Given: 有効な入力で、同一IDの航空機が存在しない
            CreateAircraftServiceInput input = validInput();

            when(aircraftMapper.selectByPrimaryKey("SEO123")).thenReturn(null);

            // When: 航空機登録を実行する
            CreateAircraftServiceOutput output = createAircraftService.create(input);

            // Then: 入力内容が未削除状態で登録される
            assertThat(output).isNotNull();

            ArgumentCaptor<Aircraft> insertCaptor = ArgumentCaptor.forClass(Aircraft.class);
            verify(aircraftMapper).insertSelective(insertCaptor.capture());

            Aircraft inserted = insertCaptor.getValue();
            assertThat(inserted.getAircraftId()).isEqualTo("SEO123");
            assertThat(inserted.getAircraftName()).isEqualTo("Boeing-777");
            assertThat(inserted.getRowCount()).isEqualTo(10);
            assertThat(inserted.getColumnCount()).isEqualTo(6);
            assertThat(inserted.getIsDeleted()).isFalse();
        }
    }

    @Nested
    @DisplayName("例外系")
    class Failure {

        @Test
        @DisplayName("航空機IDがない場合は例外が発生する")
        void create_failsWhenAircraftIdIsBlank() {
            // Given: 航空機IDが空白のみの入力
            CreateAircraftServiceInput input = CreateAircraftServiceInput.builder()
                    .aircraftId(" ")
                    .aircraftName("Boeing-777")
                    .rowCount(10)
                    .columnCount(6)
                    .build();

            // When: 航空機登録を実行する
            // Then: 入力値例外が発生し、照会・登録は行われない
            assertThatThrownBy(() -> createAircraftService.create(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("航空機IDを入力してください。");
            verify(aircraftMapper, never()).selectByPrimaryKey(any());
            verify(aircraftMapper, never()).insertSelective(any());
        }

        @Test
        @DisplayName("航空機名がない場合は例外が発生する")
        void create_failsWhenAircraftNameIsBlank() {
            // Given: 航空機名が空文字の入力
            CreateAircraftServiceInput input = CreateAircraftServiceInput.builder()
                    .aircraftId("SEO123")
                    .aircraftName("")
                    .rowCount(10)
                    .columnCount(6)
                    .build();

            // When: 航空機登録を実行する
            // Then: 入力値例外が発生し、照会・登録は行われない
            assertThatThrownBy(() -> createAircraftService.create(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("航空機名を入力してください。");
            verify(aircraftMapper, never()).selectByPrimaryKey(any());
            verify(aircraftMapper, never()).insertSelective(any());
        }

        @Test
        @DisplayName("座席の行数がない場合は例外が発生する")
        void create_failsWhenRowCountIsNull() {
            // Given: 座席の行数が null の入力
            CreateAircraftServiceInput input = CreateAircraftServiceInput.builder()
                    .aircraftId("SEO123")
                    .aircraftName("Boeing-777")
                    .rowCount(null)
                    .columnCount(6)
                    .build();

            // When: 航空機登録を実行する
            // Then: 入力値例外が発生し、照会・登録は行われない
            assertThatThrownBy(() -> createAircraftService.create(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("座席の行数を入力してください。");
            verify(aircraftMapper, never()).selectByPrimaryKey(any());
            verify(aircraftMapper, never()).insertSelective(any());
        }

        @Test
        @DisplayName("座席の列数がない場合は例外が発生する")
        void create_failsWhenColumnCountIsNull() {
            // Given: 座席の列数が null の入力
            CreateAircraftServiceInput input = CreateAircraftServiceInput.builder()
                    .aircraftId("SEO123")
                    .aircraftName("Boeing-777")
                    .rowCount(10)
                    .columnCount(null)
                    .build();

            // When: 航空機登録を実行する
            // Then: 入力値例外が発生し、照会・登録は行われない
            assertThatThrownBy(() -> createAircraftService.create(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("座席の列数を入力してください。");
            verify(aircraftMapper, never()).selectByPrimaryKey(any());
            verify(aircraftMapper, never()).insertSelective(any());
        }

        @Test
        @DisplayName("座席の行数が範囲外の場合は例外が発生する")
        void create_failsWhenRowCountIsOutOfRange() {
            // Given: 座席の行数が下限未満の入力
            CreateAircraftServiceInput input = CreateAircraftServiceInput.builder()
                    .aircraftId("SEO123")
                    .aircraftName("Boeing-777")
                    .rowCount(0)
                    .columnCount(6)
                    .build();

            // When: 航空機登録を実行する
            // Then: 入力値例外が発生し、照会・登録は行われない
            assertThatThrownBy(() -> createAircraftService.create(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("座席の行数は1〜20の範囲で入力してください。");
            verify(aircraftMapper, never()).selectByPrimaryKey(any());
            verify(aircraftMapper, never()).insertSelective(any());
        }

        @Test
        @DisplayName("座席の列数が範囲外の場合は例外が発生する")
        void create_failsWhenColumnCountIsOutOfRange() {
            // Given: 座席の列数が上限超過の入力
            CreateAircraftServiceInput input = CreateAircraftServiceInput.builder()
                    .aircraftId("SEO123")
                    .aircraftName("Boeing-777")
                    .rowCount(10)
                    .columnCount(10)
                    .build();

            // When: 航空機登録を実行する
            // Then: 入力値例外が発生し、照会・登録は行われない
            assertThatThrownBy(() -> createAircraftService.create(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("座席の列数は1〜9の範囲で入力してください。");
            verify(aircraftMapper, never()).selectByPrimaryKey(any());
            verify(aircraftMapper, never()).insertSelective(any());
        }

        @Test
        @DisplayName("既に登録されている航空機IDの場合は例外が発生する")
        void create_failsWhenAircraftIdIsDuplicated() {
            // Given: 同一IDの航空機が既に存在する
            CreateAircraftServiceInput input = validInput();

            when(aircraftMapper.selectByPrimaryKey("SEO123")).thenReturn(existingAircraft());

            // When: 航空機登録を実行する
            // Then: 重複例外が発生し、登録は行われない
            assertThatThrownBy(() -> createAircraftService.create(input))
                    .isInstanceOfSatisfying(DuplicateException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo(ErrorCode.DUPLICATE_AIRCRAFT_ID.getMessage());
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_AIRCRAFT_ID);
                    });
            verify(aircraftMapper, never()).insertSelective(any());
        }

        @Test
        @DisplayName("論理削除済みの航空機IDの場合は例外が発生する")
        void create_failsWhenAircraftIdIsDeleted() {
            // Given: 同一IDの論理削除済み航空機が残っている
            CreateAircraftServiceInput input = validInput();

            Aircraft deletedAircraft = existingAircraft();
            deletedAircraft.setIsDeleted(true);

            when(aircraftMapper.selectByPrimaryKey("SEO123")).thenReturn(deletedAircraft);

            // When: 航空機登録を実行する
            // Then: PKが残っているため重複例外が発生し、登録は行われない
            assertThatThrownBy(() -> createAircraftService.create(input))
                    .isInstanceOfSatisfying(DuplicateException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo(ErrorCode.DUPLICATE_AIRCRAFT_ID.getMessage());
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_AIRCRAFT_ID);
                    });
            verify(aircraftMapper, never()).insertSelective(any());
        }
    }

    /** 正常系で共通利用する有効な入力データを返す。 */
    private CreateAircraftServiceInput validInput() {
        return CreateAircraftServiceInput.builder()
                .aircraftId("SEO123")
                .aircraftName("Boeing-777")
                .rowCount(10)
                .columnCount(6)
                .build();
    }

    /** 重複チェック用の既存航空機テストデータを返す。 */
    private Aircraft existingAircraft() {
        Aircraft aircraft = new Aircraft();
        aircraft.setAircraftId("SEO123");
        aircraft.setAircraftName("Boeing-777");
        aircraft.setRowCount(10);
        aircraft.setColumnCount(6);
        aircraft.setIsDeleted(false);
        return aircraft;
    }
}
