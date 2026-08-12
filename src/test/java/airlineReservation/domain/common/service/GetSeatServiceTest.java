package airlineReservation.domain.common.service;

import airlineReservation.domain.common.serviceInput.GetSeatServiceInput;
import airlineReservation.domain.common.serviceOutput.GetSeatServiceOutput;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.entity.Schedule;
import airlineReservation.infra.entity.ScheduleSeat;
import airlineReservation.infra.entity.ScheduleSeatExample;
import airlineReservation.infra.mapper.ScheduleMapper;
import airlineReservation.infra.mapper.ScheduleSeatMapper;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * GetSeatService の単体テスト。
 * ScheduleMapper / ScheduleSeatMapper をモックし、
 * 入力検証・スケジュール存在確認・座席一覧取得を検証する。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("座席一覧取得サービス")
class GetSeatServiceTest {

    @Mock
    private ScheduleSeatMapper scheduleSeatMapper;

    @Mock
    private ScheduleMapper scheduleMapper;

    @InjectMocks
    private GetSeatService getSeatService;

    @Nested
    @DisplayName("正常系")
    class Success {

        @Test
        @DisplayName("有効なスケジュールで座席が存在する場合は一覧を返す")
        void getSeatList_succeedsWhenSeatsExist() {
            // Given: 有効なスケジュールと座席が存在する
            GetSeatServiceInput input = validInput();
            ScheduleSeat seat1A = availableSeat(1, "1A");
            ScheduleSeat seat1B = availableSeat(2, "1B");

            when(scheduleMapper.selectByPrimaryKey(100)).thenReturn(existingSchedule());
            when(scheduleSeatMapper.selectByExample(any(ScheduleSeatExample.class)))
                    .thenReturn(List.of(seat1A, seat1B));

            // When: 座席一覧取得を実行する
            GetSeatServiceOutput output = getSeatService.getSeatList(input);

            // Then: 座席一覧が返却され、スケジュールIDと座席名順で照会される
            assertThat(output.getScheduleId()).isEqualTo(100);
            assertThat(output.getSeatList()).containsExactly(seat1A, seat1B);

            ArgumentCaptor<ScheduleSeatExample> exampleCaptor = ArgumentCaptor.forClass(ScheduleSeatExample.class);
            verify(scheduleSeatMapper).selectByExample(exampleCaptor.capture());

            ScheduleSeatExample example = exampleCaptor.getValue();
            assertThat(example.getOrderByClause()).isEqualTo("seat_name ASC");
            assertThat(example.getOredCriteria()).hasSize(1);
            assertThat(example.getOredCriteria().get(0).getAllCriteria())
                    .anySatisfy(criterion -> {
                        assertThat(criterion.getCondition()).contains("schedule_id");
                        assertThat(criterion.getValue()).isEqualTo(100);
                    });
        }

        @Test
        @DisplayName("座席が存在しない場合は空リストを返す")
        void getSeatList_succeedsWithEmptyList() {
            // Given: スケジュールは存在するが座席が未登録
            GetSeatServiceInput input = validInput();

            when(scheduleMapper.selectByPrimaryKey(100)).thenReturn(existingSchedule());
            when(scheduleSeatMapper.selectByExample(any(ScheduleSeatExample.class)))
                    .thenReturn(List.of());

            // When: 座席一覧取得を実行する
            GetSeatServiceOutput output = getSeatService.getSeatList(input);

            // Then: 空リストが返却される
            assertThat(output.getScheduleId()).isEqualTo(100);
            assertThat(output.getSeatList()).isEmpty();
        }
    }

    @Nested
    @DisplayName("例外系")
    class Failure {

        @Test
        @DisplayName("スケジュールIDがない場合は例外が発生する")
        void getSeatList_failsWhenScheduleIdIsNull() {
            // Given: スケジュールIDが null の入力
            GetSeatServiceInput input = GetSeatServiceInput.builder()
                    .scheduleId(null)
                    .build();

            // When: 座席一覧取得を実行する
            // Then: 入力値例外が発生し、照会は行われない
            assertThatThrownBy(() -> getSeatService.getSeatList(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("スケジュールIDを入力してください。");
            verify(scheduleMapper, never()).selectByPrimaryKey(anyInt());
            verify(scheduleSeatMapper, never()).selectByExample(any());
        }

        @Test
        @DisplayName("存在しないスケジュールの場合は例外が発生する")
        void getSeatList_failsWhenScheduleDoesNotExist() {
            // Given: 指定IDの運航スケジュールが存在しない
            GetSeatServiceInput input = validInput();

            when(scheduleMapper.selectByPrimaryKey(100)).thenReturn(null);

            // When: 座席一覧取得を実行する
            // Then: NotFound 例外が発生し、座席照会は行われない
            assertThatThrownBy(() -> getSeatService.getSeatList(input))
                    .isInstanceOfSatisfying(NotFoundException.class, ex -> {
                        assertThat(ex.getMessage()).isEqualTo("存在しない運航スケジュールです: 100");
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.SCHEDULE_NOT_FOUND);
                    });
            verify(scheduleSeatMapper, never()).selectByExample(any());
        }

        @Test
        @DisplayName("キャンセル済みスケジュールの場合は例外が発生する")
        void getSeatList_failsWhenScheduleIsCancelled() {
            // Given: 運航スケジュールがキャンセル済み
            GetSeatServiceInput input = validInput();
            Schedule cancelled = existingSchedule();
            cancelled.setStatus(Const.BOOKING_STATUS.CANCELLED);

            when(scheduleMapper.selectByPrimaryKey(100)).thenReturn(cancelled);

            // When: 座席一覧取得を実行する
            // Then: 入力値例外が発生し、座席照会は行われない
            assertThatThrownBy(() -> getSeatService.getSeatList(input))
                    .isInstanceOf(InvalidInputValueException.class)
                    .hasMessage("キャンセルされた運航スケジュールです: 100");
            verify(scheduleSeatMapper, never()).selectByExample(any());
        }
    }

    /** 正常系で共通利用する有効な座席検索入力を返す。 */
    private GetSeatServiceInput validInput() {
        return GetSeatServiceInput.builder()
                .scheduleId(100)
                .build();
    }

    /** 照会対象となる有効な運航スケジュールのテストデータを返す。 */
    private Schedule existingSchedule() {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(100);
        schedule.setStatus("SCHEDULED");
        return schedule;
    }

    /** AVAILABLE 状態の座席テストデータを返す。 */
    private ScheduleSeat availableSeat(Integer scheduledSeatNo, String seatName) {
        ScheduleSeat seat = new ScheduleSeat();
        seat.setScheduledSeatNo(scheduledSeatNo);
        seat.setScheduleId(100);
        seat.setSeatName(seatName);
        seat.setStatus(Const.BOOKING_STATUS.AVAILABLE);
        return seat;
    }
}
