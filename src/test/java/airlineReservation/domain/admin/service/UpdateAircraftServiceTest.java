package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.UpdateAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.UpdateAircraftServiceOutput;
import airlineReservation.domain.admin.validator.AircraftInputValidator;
import airlineReservation.domain.admin.vo.AircraftDeletionConstraintVo;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.ConflictException;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.entity.Aircraft;
import airlineReservation.infra.mapper.AircraftMapper;
import airlineReservation.infra.mapper.customMapper.AircraftCustomMapper;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * UpdateAircraftService の単体テスト。
 * AircraftMapper / AircraftCustomMapper をモックし、
 * 入力検証・存在確認・参照チェック・更新処理を検証する。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("航空機更新サービス")
class UpdateAircraftServiceTest {

    @Mock
    private AircraftMapper aircraftMapper;

    @Mock
    private AircraftCustomMapper aircraftCustomMapper;

    @Spy
    private AircraftInputValidator aircraftInputValidator = new AircraftInputValidator();

    @InjectMocks
    private UpdateAircraftService updateAircraftService;

    @Nested
    @DisplayName("正常系")
    class Success {

        @Test
        @DisplayName("有効な入力で航空機を更新すると成功する")
        void update_succeedsWithValidInput() {
            // Given: 未削除の航空機が存在し、予約・運航・テンプレートもない
            UpdateAircraftServiceInput input = validInput();

            when(aircraftMapper.selectByPrimaryKey("SEO123")).thenReturn(existingAircraft());
            when(aircraftCustomMapper.selectDeletionConstraints(
                    eq("SEO123"),
                    eq(Const.BOOKING_STATUS.CANCELLED),
                    eq(Const.SCHEDULE_STATUS.CANCELLED)
            )).thenReturn(noConstraints());

            // When: 航空機更新を実行する
            UpdateAircraftServiceOutput output = updateAircraftService.update(input);

            // Then: 機種名・座席数が更新される
            assertThat(output).isNotNull();

            ArgumentCaptor<Aircraft> updateCaptor = ArgumentCaptor.forClass(Aircraft.class);
            verify(aircraftMapper).updateByPrimaryKeySelective(updateCaptor.capture());

            Aircraft updated = updateCaptor.getValue();
            assertThat(updated.getAircraftId()).isEqualTo("SEO123");
            assertThat(updated.getAircraftName()).isEqualTo("Boeing-787");
            assertThat(updated.getRowCount()).isEqualTo(12);
            assertThat(updated.getColumnCount()).isEqualTo(9);
        }
    }

    @Nested
    @DisplayName("例外系")
    class Failure {

        @Test
        @DisplayName("航空機IDがない場合は例外が発生する")
        void update_failsWhenAircraftIdIsBlank() {
            // Given: 航空機IDが空白のみの入力
            UpdateAircraftServiceInput input = UpdateAircraftServiceInput.builder()
                    .aircraftId(" ")
                    .aircraftName("Boeing-787")
                    .rowCount(12)
                    .columnCount(9)
                    .build();

            // When: 航空機更新を実行する
            // Then: 入力値例外が発生し、照会・更新は行われない
            assertThatThrownBy(() -> updateAircraftService.update(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("航空機IDを入力してください。");
            verify(aircraftMapper, never()).selectByPrimaryKey(any());
            verify(aircraftMapper, never()).updateByPrimaryKeySelective(any());
        }

        @Test
        @DisplayName("航空機名がない場合は例外が発生する")
        void update_failsWhenAircraftNameIsBlank() {
            // Given: 航空機名が空文字の入力
            UpdateAircraftServiceInput input = UpdateAircraftServiceInput.builder()
                    .aircraftId("SEO123")
                    .aircraftName("")
                    .rowCount(12)
                    .columnCount(9)
                    .build();

            // When: 航空機更新を実行する
            // Then: 入力値例外が発生し、照会・更新は行われない
            assertThatThrownBy(() -> updateAircraftService.update(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("航空機名を入力してください。");
            verify(aircraftMapper, never()).selectByPrimaryKey(any());
            verify(aircraftMapper, never()).updateByPrimaryKeySelective(any());
        }

        @Test
        @DisplayName("座席の行数がない場合は例外が発生する")
        void update_failsWhenRowCountIsNull() {
            // Given: 座席の行数が null の入力
            UpdateAircraftServiceInput input = UpdateAircraftServiceInput.builder()
                    .aircraftId("SEO123")
                    .aircraftName("Boeing-787")
                    .rowCount(null)
                    .columnCount(9)
                    .build();

            // When: 航空機更新を実行する
            // Then: 入力値例外が発生し、照会・更新は行われない
            assertThatThrownBy(() -> updateAircraftService.update(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("座席の行数を入力してください。");
            verify(aircraftMapper, never()).selectByPrimaryKey(any());
            verify(aircraftMapper, never()).updateByPrimaryKeySelective(any());
        }

        @Test
        @DisplayName("座席の列数がない場合は例外が発生する")
        void update_failsWhenColumnCountIsNull() {
            // Given: 座席の列数が null の入力
            UpdateAircraftServiceInput input = UpdateAircraftServiceInput.builder()
                    .aircraftId("SEO123")
                    .aircraftName("Boeing-787")
                    .rowCount(12)
                    .columnCount(null)
                    .build();

            // When: 航空機更新を実行する
            // Then: 入力値例外が発生し、照会・更新は行われない
            assertThatThrownBy(() -> updateAircraftService.update(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("座席の列数を入力してください。");
            verify(aircraftMapper, never()).selectByPrimaryKey(any());
            verify(aircraftMapper, never()).updateByPrimaryKeySelective(any());
        }

        @Test
        @DisplayName("座席の行数が範囲外の場合は例外が発生する")
        void update_failsWhenRowCountIsOutOfRange() {
            // Given: 座席の行数が上限超過の入力
            UpdateAircraftServiceInput input = UpdateAircraftServiceInput.builder()
                    .aircraftId("SEO123")
                    .aircraftName("Boeing-787")
                    .rowCount(21)
                    .columnCount(9)
                    .build();

            // When: 航空機更新を実行する
            // Then: 入力値例外が発生し、照会・更新は行われない
            assertThatThrownBy(() -> updateAircraftService.update(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("座席の行数は1〜20の範囲で入力してください。");
            verify(aircraftMapper, never()).selectByPrimaryKey(any());
            verify(aircraftMapper, never()).updateByPrimaryKeySelective(any());
        }

        @Test
        @DisplayName("座席の列数が範囲外の場合は例外が発生する")
        void update_failsWhenColumnCountIsOutOfRange() {
            // Given: 座席の列数が下限未満の入力
            UpdateAircraftServiceInput input = UpdateAircraftServiceInput.builder()
                    .aircraftId("SEO123")
                    .aircraftName("Boeing-787")
                    .rowCount(12)
                    .columnCount(0)
                    .build();

            // When: 航空機更新を実行する
            // Then: 入力値例外が発生し、照会・更新は行われない
            assertThatThrownBy(() -> updateAircraftService.update(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("座席の列数は1〜9の範囲で入力してください。");
            verify(aircraftMapper, never()).selectByPrimaryKey(any());
            verify(aircraftMapper, never()).updateByPrimaryKeySelective(any());
        }

        @Test
        @DisplayName("存在しない航空機の場合は例外が発生する")
        void update_failsWhenAircraftDoesNotExist() {
            // Given: 指定IDの航空機が存在しない
            UpdateAircraftServiceInput input = validInput();

            when(aircraftMapper.selectByPrimaryKey("SEO123")).thenReturn(null);

            // When: 航空機更新を実行する
            // Then: NotFound 例外が発生し、参照確認・更新は行われない
            assertThatThrownBy(() -> updateAircraftService.update(input))
                    .isInstanceOfSatisfying(NotFoundException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo("登録されていない航空機です: SEO123");
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.AIRCRAFT_NOT_FOUND);
                    });
            verify(aircraftCustomMapper, never()).selectDeletionConstraints(any(), any(), any());
            verify(aircraftMapper, never()).updateByPrimaryKeySelective(any());
        }

        @Test
        @DisplayName("既に削除済みの航空機の場合は例外が発生する")
        void update_failsWhenAircraftIsAlreadyDeleted() {
            // Given: 既に論理削除済みの航空機
            UpdateAircraftServiceInput input = validInput();

            Aircraft deletedAircraft = existingAircraft();
            deletedAircraft.setIsDeleted(true);

            when(aircraftMapper.selectByPrimaryKey("SEO123")).thenReturn(deletedAircraft);

            // When: 航空機更新を実行する
            // Then: NotFound 例外が発生し、参照確認・更新は行われない
            assertThatThrownBy(() -> updateAircraftService.update(input))
                    .isInstanceOfSatisfying(NotFoundException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo("登録されていない航空機です: SEO123");
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.AIRCRAFT_NOT_FOUND);
                    });
            verify(aircraftCustomMapper, never()).selectDeletionConstraints(any(), any(), any());
            verify(aircraftMapper, never()).updateByPrimaryKeySelective(any());
        }

        @Test
        @DisplayName("有効な予約がある場合は例外が発生する")
        void update_failsWhenActiveBookingExists() {
            // Given: 航空機は存在するが、キャンセル以外の有効予約が残っている
            UpdateAircraftServiceInput input = validInput();

            when(aircraftMapper.selectByPrimaryKey("SEO123")).thenReturn(existingAircraft());
            when(aircraftCustomMapper.selectDeletionConstraints(
                    eq("SEO123"),
                    eq(Const.BOOKING_STATUS.CANCELLED),
                    eq(Const.SCHEDULE_STATUS.CANCELLED)
            )).thenReturn(AircraftDeletionConstraintVo.builder()
                    .activeBookingCount(1)
                    .activeScheduleCount(0)
                    .scheduleTemplateCount(0)
                    .build());

            // When: 航空機更新を実行する
            // Then: 競合例外が発生し、更新は行われない
            assertThatThrownBy(() -> updateAircraftService.update(input))
                    .isInstanceOfSatisfying(ConflictException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo(ErrorCode.AIRCRAFT_HAS_ACTIVE_BOOKINGS.getMessage());
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.AIRCRAFT_HAS_ACTIVE_BOOKINGS);
                    });
            verify(aircraftMapper, never()).updateByPrimaryKeySelective(any());
        }

        @Test
        @DisplayName("運航スケジュールがある場合は例外が発生する")
        void update_failsWhenActiveScheduleExists() {
            // Given: 航空機は存在するが、キャンセル以外の運航スケジュールが残っている
            UpdateAircraftServiceInput input = validInput();

            when(aircraftMapper.selectByPrimaryKey("SEO123")).thenReturn(existingAircraft());
            when(aircraftCustomMapper.selectDeletionConstraints(
                    eq("SEO123"),
                    eq(Const.BOOKING_STATUS.CANCELLED),
                    eq(Const.SCHEDULE_STATUS.CANCELLED)
            )).thenReturn(AircraftDeletionConstraintVo.builder()
                    .activeBookingCount(0)
                    .activeScheduleCount(2)
                    .scheduleTemplateCount(0)
                    .build());

            // When: 航空機更新を実行する
            // Then: 競合例外が発生し、更新は行われない
            assertThatThrownBy(() -> updateAircraftService.update(input))
                    .isInstanceOfSatisfying(ConflictException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo("運航スケジュールが存在するため航空機を更新できません。");
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.AIRCRAFT_HAS_ACTIVE_SCHEDULES);
                    });
            verify(aircraftMapper, never()).updateByPrimaryKeySelective(any());
        }

        @Test
        @DisplayName("定期運航テンプレートがある場合は例外が発生する")
        void update_failsWhenScheduleTemplateExists() {
            // Given: 航空機は存在するが、定期運航テンプレートが残っている
            UpdateAircraftServiceInput input = validInput();

            when(aircraftMapper.selectByPrimaryKey("SEO123")).thenReturn(existingAircraft());
            when(aircraftCustomMapper.selectDeletionConstraints(
                    eq("SEO123"),
                    eq(Const.BOOKING_STATUS.CANCELLED),
                    eq(Const.SCHEDULE_STATUS.CANCELLED)
            )).thenReturn(AircraftDeletionConstraintVo.builder()
                    .activeBookingCount(0)
                    .activeScheduleCount(0)
                    .scheduleTemplateCount(1)
                    .build());

            // When: 航空機更新を実行する
            // Then: 競合例外が発生し、更新は行われない
            assertThatThrownBy(() -> updateAircraftService.update(input))
                    .isInstanceOfSatisfying(ConflictException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo("定期運航テンプレートが存在するため航空機を更新できません。");
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.AIRCRAFT_HAS_SCHEDULE_TEMPLATE);
                    });
            verify(aircraftMapper, never()).updateByPrimaryKeySelective(any());
        }
    }

    /** 正常系で共通利用する有効な更新入力を返す。 */
    private UpdateAircraftServiceInput validInput() {
        return UpdateAircraftServiceInput.builder()
                .aircraftId("SEO123")
                .aircraftName("Boeing-787")
                .rowCount(12)
                .columnCount(9)
                .build();
    }

    /** 更新対象となる未削除航空機のテストデータを返す。 */
    private Aircraft existingAircraft() {
        Aircraft aircraft = new Aircraft();
        aircraft.setAircraftId("SEO123");
        aircraft.setAircraftName("Boeing-777");
        aircraft.setRowCount(10);
        aircraft.setColumnCount(6);
        aircraft.setIsDeleted(false);
        return aircraft;
    }

    /** 更新を妨げない参照件数を返す。 */
    private AircraftDeletionConstraintVo noConstraints() {
        return AircraftDeletionConstraintVo.builder()
                .activeBookingCount(0)
                .activeScheduleCount(0)
                .scheduleTemplateCount(0)
                .build();
    }
}
