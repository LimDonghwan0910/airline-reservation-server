package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.CreateScheduleTemplateServiceInput;
import airlineReservation.domain.admin.serviceOutput.CreateScheduleTemplateServiceOutput;
import airlineReservation.domain.admin.validator.ScheduleReferenceValidator;
import airlineReservation.infra.dto.CreateScheduleTemplateRequestDaysOfWeek;
import airlineReservation.infra.entity.Schedule;
import airlineReservation.infra.entity.ScheduleExample;
import airlineReservation.infra.entity.ScheduleTemplates;
import airlineReservation.infra.entity.ScheduleTemplatesExample;
import airlineReservation.infra.mapper.ScheduleMapper;
import airlineReservation.infra.mapper.ScheduleTemplatesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 定期運航テンプレート作成サービス。
 *
 * [処理フロー]
 * 1) schedule_templates テーブルに「定期運航ルール（テンプレート）」を1件保存
 * 2) 開始日〜終了日の日付を1日ずつ走査
 * 3) その日の曜日が選択曜日（mon〜sun）であれば schedules テーブルに実運航を1件保存
 *
 * 例) 2026-07-01 ~ 2026-07-31、火/木選択、09:00出発
 *   → schedule_templates 1件 + 当該期間の全火曜日/木曜日 schedules N件を生成
 */
@Service
@RequiredArgsConstructor
public class CreateScheduleTemplateService {

    /** schedules.status のデフォルト値: 予定（SCHEDULED） */
    private static final String STATUS_SCHEDULED = "SCHEDULED";

    private final ScheduleTemplatesMapper scheduleTemplatesMapper;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleReferenceValidator scheduleReferenceValidator;
    private final ScheduleSeatProvisioningService scheduleSeatProvisioningService;

    /**
     * 定期運航テンプレート + 実スケジュールを一括作成する。
     * @Transactional: テンプレート/スケジュールのいずれかが失敗した場合、全体をロールバックする
     */
    @Transactional
    public CreateScheduleTemplateServiceOutput create(CreateScheduleTemplateServiceInput input) {
        // 1. 航空機・空港が DB に存在するか検証（無ければ例外）
        scheduleReferenceValidator.validateAircraft(input.getAircraftId());
        scheduleReferenceValidator.validateAirport(input.getDepartureAirportId());
        scheduleReferenceValidator.validateAirport(input.getArrivalAirportId());

        CreateScheduleTemplateRequestDaysOfWeek daysOfWeek = input.getDaysOfWeek();

        // 2. schedule_templates にテンプレートを1件保存 → 生成された template_id を返す
        Integer templateId = insertScheduleTemplate(input, daysOfWeek);

        // 3. 開始日から終了日まで1日ずつ走査し、選択曜日なら schedules に保存
        LocalDate currentDate = input.getStartDate();
        while (!currentDate.isAfter(input.getEndDate())) {
            if (isSelectedDayOfWeek(currentDate.getDayOfWeek(), daysOfWeek)) {
                insertSchedule(templateId, input, currentDate);
            }
            currentDate = currentDate.plusDays(1);
        }

        return CreateScheduleTemplateServiceOutput.builder()
                .build();
    }

    /**
     * schedule_templates（定期運航テンプレート）を1件 INSERT する。
     * テンプレートは「いつ、どの曜日、何時に運航するか」のルールのみを保存する。
     */
    private Integer insertScheduleTemplate(
            CreateScheduleTemplateServiceInput input,
            CreateScheduleTemplateRequestDaysOfWeek daysOfWeek) {
        ScheduleTemplates template = new ScheduleTemplates();
        template.setAircraftId(input.getAircraftId());
        template.setDepartureAirportId(input.getDepartureAirportId());
        template.setArrivalAirportId(input.getArrivalAirportId());
        template.setStartDate(input.getStartDate());
        template.setEndDate(input.getEndDate());
        template.setDepartureTime(input.getDepartureTime());
        template.setArrivalTime(input.getArrivalTime());
        template.setPrice(input.getPrice());

        // API の daysOfWeek（mon〜sun）→ DB カラム is_monday 〜 is_sunday へ変換
        template.setIsMonday(isDaySelected(daysOfWeek, DayOfWeek.MONDAY));
        template.setIsTuesday(isDaySelected(daysOfWeek, DayOfWeek.TUESDAY));
        template.setIsWednesday(isDaySelected(daysOfWeek, DayOfWeek.WEDNESDAY));
        template.setIsThursday(isDaySelected(daysOfWeek, DayOfWeek.THURSDAY));
        template.setIsFriday(isDaySelected(daysOfWeek, DayOfWeek.FRIDAY));
        template.setIsSaturday(isDaySelected(daysOfWeek, DayOfWeek.SATURDAY));
        template.setIsSunday(isDaySelected(daysOfWeek, DayOfWeek.SUNDAY));

        scheduleTemplatesMapper.insertSelective(template);

        // insertSelective 後に templateId が entity へ自動設定されていればそのまま返す
        if (template.getTemplateId() != null) {
            return template.getTemplateId();
        }

        // MyBatis 設定により PK が entity に埋まらない場合があるため、直前に挿入したデータで再検索する
        ScheduleTemplatesExample example = new ScheduleTemplatesExample();
        example.createCriteria()
                .andAircraftIdEqualTo(template.getAircraftId())
                .andDepartureAirportIdEqualTo(template.getDepartureAirportId())
                .andArrivalAirportIdEqualTo(template.getArrivalAirportId())
                .andStartDateEqualTo(template.getStartDate())
                .andEndDateEqualTo(template.getEndDate());
        example.setOrderByClause("template_id DESC");

        return scheduleTemplatesMapper.selectByExample(example).stream()
                .findFirst()
                .map(ScheduleTemplates::getTemplateId)
                .orElseThrow(() -> new IllegalStateException("schedule_templates の template_id 生成に失敗しました。"));
    }

    /**
     * schedules（実運航スケジュール）を1件 INSERT する。
     *
     * @param templateId  紐づくテンプレート PK（schedule_templates.template_id）
     * @param flightDate  このスケジュールの運航日（ループで1日ずつ渡される値）
     */
    private void insertSchedule(
            Integer templateId,
            CreateScheduleTemplateServiceInput input,
            LocalDate flightDate) {
        // 日付 + 時刻 → departure_datetime（例: 2026-07-22 09:00:00）
        LocalDateTime departureDateTime = LocalDateTime.of(flightDate, input.getDepartureTime());

        // 到着時刻が出発時刻より早い場合は翌日到着（例: 23:00出発 → 01:00 翌日到着）
        LocalDate arrivalDate = input.getArrivalTime().isBefore(input.getDepartureTime())
                ? flightDate.plusDays(1)
                : flightDate;
        LocalDateTime arrivalDateTime = LocalDateTime.of(arrivalDate, input.getArrivalTime());

        Schedule schedule = new Schedule();
        schedule.setTemplateId(templateId);
        schedule.setAircraftId(input.getAircraftId());
        schedule.setDepartureAirportId(input.getDepartureAirportId());
        schedule.setArrivalAirportId(input.getArrivalAirportId());
        schedule.setDepartureDatetime(departureDateTime);
        schedule.setArrivalDatetime(arrivalDateTime);
        schedule.setStatus(STATUS_SCHEDULED);

        scheduleMapper.insertSelective(schedule);

        Integer scheduleId = resolveScheduleId(schedule);
        scheduleSeatProvisioningService.provisionForSchedule(scheduleId, input.getAircraftId());
    }

    /**
     * insertSelective 後に schedule_id を返す。
     * MyBatis 設定により PK が entity に埋まらない場合があるため再検索する。
     */
    private Integer resolveScheduleId(Schedule schedule) {
        if (schedule.getScheduleId() != null) {
            return schedule.getScheduleId();
        }

        ScheduleExample example = new ScheduleExample();
        example.createCriteria()
                .andTemplateIdEqualTo(schedule.getTemplateId())
                .andDepartureDatetimeEqualTo(schedule.getDepartureDatetime())
                .andArrivalDatetimeEqualTo(schedule.getArrivalDatetime())
                .andAircraftIdEqualTo(schedule.getAircraftId());
        example.setOrderByClause("schedule_id DESC");

        return scheduleMapper.selectByExample(example).stream()
                .findFirst()
                .map(Schedule::getScheduleId)
                .orElseThrow(() -> new IllegalStateException("schedules の schedule_id 生成に失敗しました。"));
    }

    /** 特定日付の曜日がユーザーが選択した曜日かどうかを確認する */
    private boolean isSelectedDayOfWeek(DayOfWeek dayOfWeek, CreateScheduleTemplateRequestDaysOfWeek daysOfWeek) {
        return isDaySelected(daysOfWeek, dayOfWeek);
    }

    /**
     * daysOfWeek オブジェクトで該当曜日フラグ（mon〜sun）が true かどうかを確認する。
     * null または false の場合は運航しない曜日とみなす。
     */
    private boolean isDaySelected(CreateScheduleTemplateRequestDaysOfWeek daysOfWeek, DayOfWeek dayOfWeek) {
        if (daysOfWeek == null) {
            return false;
        }

        return switch (dayOfWeek) {
            case MONDAY -> Boolean.TRUE.equals(daysOfWeek.getMon());
            case TUESDAY -> Boolean.TRUE.equals(daysOfWeek.getTue());
            case WEDNESDAY -> Boolean.TRUE.equals(daysOfWeek.getWed());
            case THURSDAY -> Boolean.TRUE.equals(daysOfWeek.getThu());
            case FRIDAY -> Boolean.TRUE.equals(daysOfWeek.getFri());
            case SATURDAY -> Boolean.TRUE.equals(daysOfWeek.getSat());
            case SUNDAY -> Boolean.TRUE.equals(daysOfWeek.getSun());
        };
    }
}
