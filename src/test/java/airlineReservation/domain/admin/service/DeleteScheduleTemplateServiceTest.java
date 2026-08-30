package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.DeleteScheduleTemplateServiceInput;
import airlineReservation.domain.admin.serviceOutput.DeleteScheduleTemplateServiceOutput;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.ConflictException;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.entity.Schedule;
import airlineReservation.infra.entity.ScheduleExample;
import airlineReservation.infra.entity.ScheduleTemplates;
import airlineReservation.infra.mapper.ScheduleMapper;
import airlineReservation.infra.mapper.ScheduleTemplatesMapper;
import airlineReservation.infra.mapper.customMapper.ScheduleTemplateCustomMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * DeleteScheduleTemplateService の単体テスト。
 * Mapper をモックし、入力検証・存在確認・有効予約チェック・削除を検証する。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("定期運航テンプレート削除サービス")
class DeleteScheduleTemplateServiceTest {

    @Mock
    private ScheduleTemplatesMapper scheduleTemplatesMapper;

    @Mock
    private ScheduleMapper scheduleMapper;

    @Mock
    private ScheduleTemplateCustomMapper scheduleTemplateCustomMapper;

    @Mock
    private ScheduleSeatProvisioningService scheduleSeatProvisioningService;

    @InjectMocks
    private DeleteScheduleTemplateService deleteScheduleTemplateService;

    @Nested
    @DisplayName("正常系")
    class Success {

        @Test
        @DisplayName("有効な入力で削除するとテンプレートが削除されスケジュールがキャンセルされる")
        void delete_succeedsWithValidInput() {
            // Given: テンプレートが存在し、紐づくスケジュールに有効予約もない
            DeleteScheduleTemplateServiceInput input = validInput();
            Schedule linkedSchedule = linkedSchedule();

            when(scheduleTemplateCustomMapper.selectByIdForUpdate(1)).thenReturn(existingTemplate());
            when(scheduleTemplateCustomMapper.countActiveBookings(
                    eq(1),
                    eq(Const.BOOKING_STATUS.CANCELLED)
            )).thenReturn(0L);
            when(scheduleMapper.selectByExample(any(ScheduleExample.class))).thenReturn(List.of(linkedSchedule));

            // When: 定期運航テンプレート削除を実行する
            DeleteScheduleTemplateServiceOutput output = deleteScheduleTemplateService.delete(input);

            // Then: 行をロックしたうえで紐づくスケジュールはキャンセルされ、テンプレートは削除される
            assertThat(output).isNotNull();

            InOrder inOrder = inOrder(scheduleTemplateCustomMapper);
            inOrder.verify(scheduleTemplateCustomMapper).selectByIdForUpdate(1);
            inOrder.verify(scheduleTemplateCustomMapper).lockLinkedSchedules(1);
            inOrder.verify(scheduleTemplateCustomMapper).countActiveBookings(
                    eq(1),
                    eq(Const.BOOKING_STATUS.CANCELLED)
            );

            ArgumentCaptor<Schedule> updateCaptor = ArgumentCaptor.forClass(Schedule.class);
            verify(scheduleMapper).updateByPrimaryKeySelective(updateCaptor.capture());
            assertThat(updateCaptor.getValue().getScheduleId()).isEqualTo(10);
            assertThat(updateCaptor.getValue().getStatus()).isEqualTo(Const.SCHEDULE_STATUS.CANCELLED);

            verify(scheduleSeatProvisioningService).cancelForSchedule(10);
            verify(scheduleTemplatesMapper).deleteByPrimaryKey(1);
        }
    }

    @Nested
    @DisplayName("例外系")
    class Failure {

        @Test
        @DisplayName("テンプレートIDがない場合は例外が発生する")
        void delete_failsWhenTemplateIdIsNull() {
            // Given: テンプレートIDが未入力
            DeleteScheduleTemplateServiceInput input = DeleteScheduleTemplateServiceInput.builder()
                    .templateId(null)
                    .build();

            // When: 定期運航テンプレート削除を実行する
            // Then: 入力値例外が発生し、照会・削除は行われない
            assertThatThrownBy(() -> deleteScheduleTemplateService.delete(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("テンプレートIDを入力してください。");
            verify(scheduleTemplateCustomMapper, never()).selectByIdForUpdate(any());
            verify(scheduleTemplatesMapper, never()).deleteByPrimaryKey(any());
        }

        @Test
        @DisplayName("存在しないテンプレートの場合は例外が発生する")
        void delete_failsWhenTemplateDoesNotExist() {
            // Given: 指定IDのテンプレートが存在しない
            DeleteScheduleTemplateServiceInput input = validInput();

            when(scheduleTemplateCustomMapper.selectByIdForUpdate(1)).thenReturn(null);

            // When: 定期運航テンプレート削除を実行する
            // Then: NotFound 例外が発生し、参照確認・削除は行われない
            assertThatThrownBy(() -> deleteScheduleTemplateService.delete(input))
                    .isInstanceOfSatisfying(NotFoundException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo("存在しない定期運航テンプレートです: 1");
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.TEMPLATE_NOT_FOUND);
                    });
            verify(scheduleTemplateCustomMapper, never()).lockLinkedSchedules(any());
            verify(scheduleTemplateCustomMapper, never()).countActiveBookings(any(), any());
            verify(scheduleTemplatesMapper, never()).deleteByPrimaryKey(any());
        }

        @Test
        @DisplayName("有効な予約がある場合は例外が発生する")
        void delete_failsWhenActiveBookingExists() {
            // Given: テンプレートは存在するが、紐づくスケジュールにキャンセル以外の有効予約が残っている
            DeleteScheduleTemplateServiceInput input = validInput();

            when(scheduleTemplateCustomMapper.selectByIdForUpdate(1)).thenReturn(existingTemplate());
            when(scheduleTemplateCustomMapper.countActiveBookings(
                    eq(1),
                    eq(Const.BOOKING_STATUS.CANCELLED)
            )).thenReturn(1L);

            // When: 定期運航テンプレート削除を実行する
            // Then: 競合例外が発生し、キャンセル・削除は行われない
            assertThatThrownBy(() -> deleteScheduleTemplateService.delete(input))
                    .isInstanceOfSatisfying(ConflictException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo(ErrorCode.SCHEDULE_TEMPLATE_HAS_ACTIVE_BOOKINGS.getMessage());
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.SCHEDULE_TEMPLATE_HAS_ACTIVE_BOOKINGS);
                    });
            verify(scheduleMapper, never()).updateByPrimaryKeySelective(any());
            verify(scheduleSeatProvisioningService, never()).cancelForSchedule(any());
            verify(scheduleTemplatesMapper, never()).deleteByPrimaryKey(any());
        }
    }

    /** 正常系で共通利用する有効な削除入力を返す。 */
    private DeleteScheduleTemplateServiceInput validInput() {
        return DeleteScheduleTemplateServiceInput.builder()
                .templateId(1)
                .build();
    }

    /** 削除対象となるテンプレートのテストデータを返す。 */
    private ScheduleTemplates existingTemplate() {
        ScheduleTemplates template = new ScheduleTemplates();
        template.setTemplateId(1);
        template.setAircraftId("SEO123");
        return template;
    }

    /** テンプレートに紐づくスケジュールのテストデータを返す。 */
    private Schedule linkedSchedule() {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(10);
        schedule.setTemplateId(1);
        schedule.setStatus(Const.SCHEDULE_STATUS.SCHEDULED);
        return schedule;
    }
}
