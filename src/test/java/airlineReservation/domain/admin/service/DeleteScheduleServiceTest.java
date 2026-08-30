package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.DeleteScheduleServiceInput;
import airlineReservation.domain.admin.serviceOutput.DeleteScheduleServiceOutput;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.ConflictException;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.entity.BookingExample;
import airlineReservation.infra.entity.Schedule;
import airlineReservation.infra.mapper.BookingMapper;
import airlineReservation.infra.mapper.ScheduleMapper;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * DeleteScheduleService の単体テスト。
 * Mapper をモックし、入力検証・存在確認・有効予約チェック・キャンセルを検証する。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("運航スケジュール削除サービス")
class DeleteScheduleServiceTest {

    @Mock
    private ScheduleMapper scheduleMapper;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private ScheduleSeatProvisioningService scheduleSeatProvisioningService;

    @InjectMocks
    private DeleteScheduleService deleteScheduleService;

    @Nested
    @DisplayName("正常系")
    class Success {

        @Test
        @DisplayName("有効な入力で削除するとスケジュールがキャンセルされる")
        void delete_succeedsWithValidInput() {
            // Given: スケジュールが存在し、有効予約もない
            DeleteScheduleServiceInput input = validInput();

            when(scheduleMapper.selectByPrimaryKey(10)).thenReturn(existingSchedule());
            when(bookingMapper.countByExample(any(BookingExample.class))).thenReturn(0L);

            // When: 運航スケジュール削除を実行する
            DeleteScheduleServiceOutput output = deleteScheduleService.delete(input);

            // Then: スケジュールと座席がキャンセルされる
            assertThat(output).isNotNull();

            ArgumentCaptor<Schedule> updateCaptor = ArgumentCaptor.forClass(Schedule.class);
            verify(scheduleMapper).updateByPrimaryKeySelective(updateCaptor.capture());
            assertThat(updateCaptor.getValue().getScheduleId()).isEqualTo(10);
            assertThat(updateCaptor.getValue().getStatus()).isEqualTo(Const.SCHEDULE_STATUS.CANCELLED);

            verify(scheduleSeatProvisioningService).cancelForSchedule(10);
        }
    }

    @Nested
    @DisplayName("例外系")
    class Failure {

        @Test
        @DisplayName("スケジュールIDがない場合は例外が発生する")
        void delete_failsWhenScheduleIdIsNull() {
            // Given: スケジュールIDが未入力
            DeleteScheduleServiceInput input = DeleteScheduleServiceInput.builder()
                    .scheduleId(null)
                    .build();

            // When: 運航スケジュール削除を実行する
            // Then: 入力値例外が発生し、照会・更新は行われない
            assertThatThrownBy(() -> deleteScheduleService.delete(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("スケジュールIDを入力してください。");
            verify(scheduleMapper, never()).selectByPrimaryKey(any());
            verify(scheduleMapper, never()).updateByPrimaryKeySelective(any());
        }

        @Test
        @DisplayName("存在しないスケジュールの場合は例外が発生する")
        void delete_failsWhenScheduleDoesNotExist() {
            // Given: 指定IDのスケジュールが存在しない
            DeleteScheduleServiceInput input = validInput();

            when(scheduleMapper.selectByPrimaryKey(10)).thenReturn(null);

            // When: 運航スケジュール削除を実行する
            // Then: NotFound 例外が発生し、予約確認・更新は行われない
            assertThatThrownBy(() -> deleteScheduleService.delete(input))
                    .isInstanceOfSatisfying(NotFoundException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo("存在しない運航スケジュールです: 10");
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.SCHEDULE_NOT_FOUND);
                    });
            verify(bookingMapper, never()).countByExample(any());
            verify(scheduleMapper, never()).updateByPrimaryKeySelective(any());
            verify(scheduleSeatProvisioningService, never()).cancelForSchedule(any());
        }

        @Test
        @DisplayName("有効な予約がある場合は例外が発生する")
        void delete_failsWhenActiveBookingExists() {
            // Given: スケジュールは存在するが、キャンセル以外の有効予約が残っている
            DeleteScheduleServiceInput input = validInput();

            when(scheduleMapper.selectByPrimaryKey(10)).thenReturn(existingSchedule());
            when(bookingMapper.countByExample(any(BookingExample.class))).thenReturn(1L);

            // When: 運航スケジュール削除を実行する
            // Then: 競合例外が発生し、キャンセルは行われない
            assertThatThrownBy(() -> deleteScheduleService.delete(input))
                    .isInstanceOfSatisfying(ConflictException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo(ErrorCode.SCHEDULE_HAS_ACTIVE_BOOKINGS.getMessage());
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.SCHEDULE_HAS_ACTIVE_BOOKINGS);
                    });
            verify(scheduleMapper, never()).updateByPrimaryKeySelective(any());
            verify(scheduleSeatProvisioningService, never()).cancelForSchedule(any());
        }
    }

    /** 正常系で共通利用する有効な削除入力を返す。 */
    private DeleteScheduleServiceInput validInput() {
        return DeleteScheduleServiceInput.builder()
                .scheduleId(10)
                .build();
    }

    /** 削除対象となる運航スケジュールのテストデータを返す。 */
    private Schedule existingSchedule() {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(10);
        schedule.setAircraftId("SEO123");
        schedule.setStatus(Const.SCHEDULE_STATUS.SCHEDULED);
        return schedule;
    }
}
