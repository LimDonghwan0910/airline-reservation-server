package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.CreateScheduleTemplateServiceInput;
import airlineReservation.domain.admin.serviceOutput.CreateScheduleTemplateServiceOutput;
import airlineReservation.domain.admin.validator.ScheduleReferenceValidator;
import airlineReservation.global.exception.DuplicateException;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.infra.dto.CreateScheduleTemplateRequestDaysOfWeek;
import airlineReservation.infra.entity.Schedule;
import airlineReservation.infra.entity.ScheduleTemplates;
import airlineReservation.infra.entity.ScheduleTemplatesExample;
import airlineReservation.infra.mapper.ScheduleMapper;
import airlineReservation.infra.mapper.ScheduleTemplatesMapper;
import airlineReservation.infra.mapper.customMapper.ScheduleTemplateCustomMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * CreateScheduleTemplateService の単体テスト。
 * Mapper をモックし、期間重複チェックと登録処理を検証する。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("定期運航テンプレート登録サービス")
class CreateScheduleTemplateServiceTest {

    @Mock
    private ScheduleTemplatesMapper scheduleTemplatesMapper;

    @Mock
    private ScheduleMapper scheduleMapper;

    @Mock
    private ScheduleReferenceValidator scheduleReferenceValidator;

    @Mock
    private ScheduleTemplateCustomMapper scheduleTemplateCustomMapper;

    @Mock
    private ScheduleSeatProvisioningService scheduleSeatProvisioningService;

    @InjectMocks
    private CreateScheduleTemplateService createScheduleTemplateService;

    @Nested
    @DisplayName("正常系")
    class Success {

        @Test
        @DisplayName("期間が重複しない場合は登録できる")
        void create_succeedsWhenPeriodDoesNotOverlap() {
            // Given: 同一便・同一区間で期間が重複するテンプレートがない
            CreateScheduleTemplateServiceInput input = validInput();

            when(scheduleTemplatesMapper.countByExample(any(ScheduleTemplatesExample.class))).thenReturn(0L);
            when(scheduleTemplatesMapper.insertSelective(any(ScheduleTemplates.class))).thenAnswer(invocation -> {
                ScheduleTemplates template = invocation.getArgument(0);
                template.setTemplateId(1);
                return 1;
            });
            when(scheduleMapper.insertSelective(any(Schedule.class))).thenAnswer(invocation -> {
                Schedule schedule = invocation.getArgument(0);
                schedule.setScheduleId(10);
                return 1;
            });

            // When: 定期運航テンプレート登録を実行する
            CreateScheduleTemplateServiceOutput output = createScheduleTemplateService.create(input);

            // Then: 同一便・同一区間のテンプレートをロックしたうえで登録される
            assertThat(output).isNotNull();
            InOrder inOrder = inOrder(scheduleTemplateCustomMapper, scheduleTemplatesMapper);
            inOrder.verify(scheduleTemplateCustomMapper).lockByAircraftAndRoute("SEO123", "ICN", "NRT");
            inOrder.verify(scheduleTemplatesMapper).countByExample(any(ScheduleTemplatesExample.class));
            verify(scheduleTemplatesMapper).insertSelective(any(ScheduleTemplates.class));
        }
    }

    @Nested
    @DisplayName("例外系")
    class Failure {

        @Test
        @DisplayName("同一便・同一区間で期間が重複するテンプレートがある場合は例外が発生する")
        void create_failsWhenPeriodOverlaps() {
            // Given: 同一便・同一区間で期間が一部重なるテンプレートが既に存在する
            CreateScheduleTemplateServiceInput input = validInput();

            when(scheduleTemplatesMapper.countByExample(any(ScheduleTemplatesExample.class))).thenReturn(1L);

            // When: 定期運航テンプレート登録を実行する
            // Then: 重複例外が発生し、登録は行われない
            assertThatThrownBy(() -> createScheduleTemplateService.create(input))
                    .isInstanceOfSatisfying(DuplicateException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo(ErrorCode.DUPLICATE_SCHEDULE_TEMPLATE.getMessage());
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_SCHEDULE_TEMPLATE);
                    });
            verify(scheduleTemplatesMapper, never()).insertSelective(any());
            verify(scheduleMapper, never()).insertSelective(any());
        }

        @Test
        @DisplayName("開始日が終了日より後の場合は例外が発生する")
        void create_failsWhenStartDateIsAfterEndDate() {
            // Given: 開始日が終了日より後の入力
            CreateScheduleTemplateServiceInput input = validInput().toBuilder()
                    .startDate(LocalDate.of(2026, 8, 31))
                    .endDate(LocalDate.of(2026, 8, 17))
                    .build();

            // When: 定期運航テンプレート登録を実行する
            // Then: 入力値例外が発生し、登録は行われない
            assertThatThrownBy(() -> createScheduleTemplateService.create(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("運航開始日は終了日以前の日付を入力してください。");
            verify(scheduleTemplatesMapper, never()).insertSelective(any());
        }

        @Test
        @DisplayName("出発空港と到着空港が同じ場合は例外が発生する")
        void create_failsWhenAirportsAreTheSame() {
            // Given: 出発空港と到着空港が同一の入力
            CreateScheduleTemplateServiceInput input = validInput().toBuilder()
                    .arrivalAirportId("ICN")
                    .build();

            // When: 定期運航テンプレート登録を実行する
            // Then: 入力値例外が発生し、登録は行われない
            assertThatThrownBy(() -> createScheduleTemplateService.create(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("出発空港と到着空港は異なる空港を選択してください。");
            verify(scheduleTemplatesMapper, never()).insertSelective(any());
        }

        @Test
        @DisplayName("運航曜日が未選択の場合は例外が発生する")
        void create_failsWhenNoDayOfWeekIsSelected() {
            // Given: 運航曜日がすべて未選択
            CreateScheduleTemplateServiceInput input = validInput().toBuilder()
                    .daysOfWeek(new CreateScheduleTemplateRequestDaysOfWeek())
                    .build();

            // When: 定期運航テンプレート登録を実行する
            // Then: 入力値例外が発生し、登録は行われない
            assertThatThrownBy(() -> createScheduleTemplateService.create(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("運航曜日を選択してください。");
            verify(scheduleTemplatesMapper, never()).insertSelective(any());
        }

        @Test
        @DisplayName("料金が0以下の場合は例外が発生する")
        void create_failsWhenPriceIsNotPositive() {
            // Given: 料金が0の入力
            CreateScheduleTemplateServiceInput input = validInput().toBuilder()
                    .price(0)
                    .build();

            // When: 定期運航テンプレート登録を実行する
            // Then: 入力値例外が発生し、登録は行われない
            assertThatThrownBy(() -> createScheduleTemplateService.create(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("料金は1以上で入力してください。");
            verify(scheduleTemplatesMapper, never()).insertSelective(any());
        }
    }

    /** 正常系で共通利用する有効な入力データを返す。 */
    private CreateScheduleTemplateServiceInput validInput() {
        CreateScheduleTemplateRequestDaysOfWeek daysOfWeek = new CreateScheduleTemplateRequestDaysOfWeek();
        daysOfWeek.setMon(true);

        return CreateScheduleTemplateServiceInput.builder()
                .aircraftId("SEO123")
                .departureAirportId("ICN")
                .arrivalAirportId("NRT")
                .startDate(LocalDate.of(2026, 8, 17))
                .endDate(LocalDate.of(2026, 8, 31))
                .departureTime(LocalTime.of(9, 0))
                .arrivalTime(LocalTime.of(11, 0))
                .price(50000)
                .daysOfWeek(daysOfWeek)
                .build();
    }
}
