package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.DeleteAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.DeleteAircraftServiceOutput;
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
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * DeleteAircraftService の単体テスト。
 * AircraftMapper / AircraftCustomMapper をモックし、
 * 入力検証・存在確認・参照チェック・論理削除を検証する。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("航空機削除サービス")
class DeleteAircraftServiceTest {

    @Mock
    private AircraftMapper aircraftMapper;

    @Mock
    private AircraftCustomMapper aircraftCustomMapper;

    @InjectMocks
    private DeleteAircraftService deleteAircraftService;

    @Nested
    @DisplayName("正常系")
    class Success {

        @Test
        @DisplayName("有効な入力で削除すると論理削除される")
        void delete_succeedsWithValidInput() {
            // Given: 未削除の航空機が存在し、予約・運航・テンプレートもない
            DeleteAircraftServiceInput input = validInput();

            when(aircraftMapper.selectByPrimaryKey("SEO123")).thenReturn(existingAircraft());
            when(aircraftCustomMapper.selectDeletionConstraints(
                    eq("SEO123"),
                    eq(Const.BOOKING_STATUS.CANCELLED),
                    eq(Const.SCHEDULE_STATUS.CANCELLED)
            )).thenReturn(noConstraints());

            // When: 航空機削除を実行する
            DeleteAircraftServiceOutput output = deleteAircraftService.delete(input);

            // Then: 論理削除フラグのみが更新される
            assertThat(output).isNotNull();

            ArgumentCaptor<Aircraft> updateCaptor = ArgumentCaptor.forClass(Aircraft.class);
            verify(aircraftMapper).updateByPrimaryKeySelective(updateCaptor.capture());

            Aircraft updated = updateCaptor.getValue();
            assertThat(updated.getAircraftId()).isEqualTo("SEO123");
            assertThat(updated.getIsDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("例外系")
    class Failure {

        @Test
        @DisplayName("航空機IDがない場合は例外が発生する")
        void delete_failsWhenAircraftIdIsBlank() {
            // Given: 航空機IDが空白のみの入力
            DeleteAircraftServiceInput input = DeleteAircraftServiceInput.builder()
                    .aircraftId(" ")
                    .build();

            // When: 航空機削除を実行する
            // Then: 入力値例外が発生し、照会・更新は行われない
            assertThatThrownBy(() -> deleteAircraftService.delete(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("航空機IDを入力してください。");
            verify(aircraftMapper, never()).selectByPrimaryKey(any());
            verify(aircraftMapper, never()).updateByPrimaryKeySelective(any());
        }

        @Test
        @DisplayName("存在しない航空機の場合は例外が発生する")
        void delete_failsWhenAircraftDoesNotExist() {
            // Given: 指定IDの航空機が存在しない
            DeleteAircraftServiceInput input = validInput();

            when(aircraftMapper.selectByPrimaryKey("SEO123")).thenReturn(null);

            // When: 航空機削除を実行する
            // Then: NotFound 例外が発生し、参照確認・更新は行われない
            assertThatThrownBy(() -> deleteAircraftService.delete(input))
                    .isInstanceOfSatisfying(NotFoundException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo("登録されていない航空機です: SEO123");
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.AIRCRAFT_NOT_FOUND);
                    });
            verify(aircraftCustomMapper, never()).selectDeletionConstraints(any(), any(), any());
            verify(aircraftMapper, never()).updateByPrimaryKeySelective(any());
        }

        @Test
        @DisplayName("既に削除済みの航空機の場合は例外が発生する")
        void delete_failsWhenAircraftIsAlreadyDeleted() {
            // Given: 既に論理削除済みの航空機
            DeleteAircraftServiceInput input = validInput();

            Aircraft deletedAircraft = existingAircraft();
            deletedAircraft.setIsDeleted(true);

            when(aircraftMapper.selectByPrimaryKey("SEO123")).thenReturn(deletedAircraft);

            // When: 航空機削除を実行する
            // Then: NotFound 例外が発生し、参照確認・更新は行われない
            assertThatThrownBy(() -> deleteAircraftService.delete(input))
                    .isInstanceOfSatisfying(NotFoundException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo("登録されていない航空機です: SEO123");
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.AIRCRAFT_NOT_FOUND);
                    });
            verify(aircraftCustomMapper, never()).selectDeletionConstraints(any(), any(), any());
            verify(aircraftMapper, never()).updateByPrimaryKeySelective(any());
        }

        @Test
        @DisplayName("有効な予約がある場合は例外が発生する")
        void delete_failsWhenActiveBookingExists() {
            // Given: 航空機は存在するが、キャンセル以外の有効予約が残っている
            DeleteAircraftServiceInput input = validInput();

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

            // When: 航空機削除を実行する
            // Then: 競合例外が発生し、削除更新は行われない
            assertThatThrownBy(() -> deleteAircraftService.delete(input))
                    .isInstanceOfSatisfying(ConflictException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo("有効な予約があるため航空機を削除できません。");
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.AIRCRAFT_HAS_ACTIVE_BOOKINGS);
                    });
            verify(aircraftMapper, never()).updateByPrimaryKeySelective(any());
        }

        @Test
        @DisplayName("運航スケジュールがある場合は例外が発生する")
        void delete_failsWhenActiveScheduleExists() {
            // Given: 航空機は存在するが、キャンセル以外の運航スケジュールが残っている
            DeleteAircraftServiceInput input = validInput();

            when(aircraftMapper.selectByPrimaryKey("SEO123")).thenReturn(existingAircraft());
            when(aircraftCustomMapper.selectDeletionConstraints(
                    eq("SEO123"),
                    eq(Const.BOOKING_STATUS.CANCELLED),
                    eq(Const.SCHEDULE_STATUS.CANCELLED)
            )).thenReturn(AircraftDeletionConstraintVo.builder()
                    .activeBookingCount(0)
                    .activeScheduleCount(1)
                    .scheduleTemplateCount(0)
                    .build());

            // When: 航空機削除を実行する
            // Then: 競合例外が発生し、削除更新は行われない
            assertThatThrownBy(() -> deleteAircraftService.delete(input))
                    .isInstanceOfSatisfying(ConflictException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo(ErrorCode.AIRCRAFT_HAS_ACTIVE_SCHEDULES.getMessage());
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.AIRCRAFT_HAS_ACTIVE_SCHEDULES);
                    });
            verify(aircraftMapper, never()).updateByPrimaryKeySelective(any());
        }

        @Test
        @DisplayName("定期運航テンプレートがある場合は例外が発生する")
        void delete_failsWhenScheduleTemplateExists() {
            // Given: 航空機は存在するが、定期運航テンプレートが残っている
            DeleteAircraftServiceInput input = validInput();

            when(aircraftMapper.selectByPrimaryKey("SEO123")).thenReturn(existingAircraft());
            when(aircraftCustomMapper.selectDeletionConstraints(
                    eq("SEO123"),
                    eq(Const.BOOKING_STATUS.CANCELLED),
                    eq(Const.SCHEDULE_STATUS.CANCELLED)
            )).thenReturn(AircraftDeletionConstraintVo.builder()
                    .activeBookingCount(0)
                    .activeScheduleCount(0)
                    .scheduleTemplateCount(3)
                    .build());

            // When: 航空機削除を実行する
            // Then: 競合例外が発生し、削除更新は行われない
            assertThatThrownBy(() -> deleteAircraftService.delete(input))
                    .isInstanceOfSatisfying(ConflictException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo(ErrorCode.AIRCRAFT_HAS_SCHEDULE_TEMPLATE.getMessage());
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.AIRCRAFT_HAS_SCHEDULE_TEMPLATE);
                    });
            verify(aircraftMapper, never()).updateByPrimaryKeySelective(any());
        }
    }

    /** 正常系で共通利用する有効な削除入力を返す。 */
    private DeleteAircraftServiceInput validInput() {
        return DeleteAircraftServiceInput.builder()
                .aircraftId("SEO123")
                .build();
    }

    /** 削除対象となる未削除航空機のテストデータを返す。 */
    private Aircraft existingAircraft() {
        Aircraft aircraft = new Aircraft();
        aircraft.setAircraftId("SEO123");
        aircraft.setAircraftName("Boeing-777");
        aircraft.setIsDeleted(false);
        return aircraft;
    }

    /** 削除を妨げない参照件数を返す。 */
    private AircraftDeletionConstraintVo noConstraints() {
        return AircraftDeletionConstraintVo.builder()
                .activeBookingCount(0)
                .activeScheduleCount(0)
                .scheduleTemplateCount(0)
                .build();
    }
}
