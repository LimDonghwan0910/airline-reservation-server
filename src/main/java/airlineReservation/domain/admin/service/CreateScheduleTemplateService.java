package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.CreateScheduleTemplateServiceInput;
import airlineReservation.domain.admin.serviceOutput.CreateScheduleTemplateServiceOutput;
import airlineReservation.domain.admin.validator.ScheduleReferenceValidator;
import airlineReservation.global.constant.Const;
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
 * 定期運航テンプレート作成処理を行うサービス。
 */
@Service
@RequiredArgsConstructor
public class CreateScheduleTemplateService {

    private final ScheduleTemplatesMapper scheduleTemplatesMapper;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleReferenceValidator scheduleReferenceValidator;
    private final ScheduleSeatProvisioningService scheduleSeatProvisioningService;

    /**
     * 定期運航テンプレートを登録し、対象期間の実スケジュールを生成する。
     *
     * @param input
     * @return serviceOutput
     * @throws InvalidInputValueException 入力項目が誤っている場合
     * @throws NotFoundException 航空機または空港が存在しない場合
     */
    @Transactional
    public CreateScheduleTemplateServiceOutput create(CreateScheduleTemplateServiceInput input) {
        scheduleReferenceValidator.validateAircraft(input.getAircraftId());
        scheduleReferenceValidator.validateAirport(input.getDepartureAirportId());
        scheduleReferenceValidator.validateAirport(input.getArrivalAirportId());

        CreateScheduleTemplateRequestDaysOfWeek daysOfWeek = input.getDaysOfWeek();

        Integer templateId = insertScheduleTemplate(input, daysOfWeek);

        // 開始日〜終了日を1日ずつ走査し、選択曜日なら schedules に保存
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
     * 定期運航テンプレートを1件登録する。
     *
     * @param input
     * @param daysOfWeek 運航曜日
     * @return 生成されたテンプレートID
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

        if (template.getTemplateId() != null) {
            return template.getTemplateId();
        }

        // MyBatis 設定により PK が entity に埋まらない場合があるため再検索する
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
     * 実運航スケジュールを1件登録し、座席を払い出す。
     *
     * @param templateId 紐づくテンプレートID
     * @param input
     * @param flightDate 運航日
     */
    private void insertSchedule(
            Integer templateId,
            CreateScheduleTemplateServiceInput input,
            LocalDate flightDate) {
        LocalDateTime departureDateTime = LocalDateTime.of(flightDate, input.getDepartureTime());

        // 到着時刻が出発時刻より早い場合は翌日到着
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
        schedule.setStatus(Const.SCHEDULE_STATUS.SCHEDULED);

        scheduleMapper.insertSelective(schedule);

        Integer scheduleId = resolveScheduleId(schedule);
        scheduleSeatProvisioningService.provisionForSchedule(scheduleId, input.getAircraftId());
    }

    /**
     * insertSelective 後の schedule_id を取得する。
     *
     * @param schedule 登録したスケジュール
     * @return schedule_id
     */
    private Integer resolveScheduleId(Schedule schedule) {
        if (schedule.getScheduleId() != null) {
            return schedule.getScheduleId();
        }

        // MyBatis 設定により PK が entity に埋まらない場合があるため再検索する
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

    /**
     * 指定曜日が選択されているかを確認する。
     *
     * @param dayOfWeek 確認対象の曜日
     * @param daysOfWeek 選択曜日
     * @return 選択されている場合 true
     */
    private boolean isSelectedDayOfWeek(DayOfWeek dayOfWeek, CreateScheduleTemplateRequestDaysOfWeek daysOfWeek) {
        return isDaySelected(daysOfWeek, dayOfWeek);
    }

    /**
     * daysOfWeek の該当曜日フラグが true かを確認する。
     *
     * @param daysOfWeek 選択曜日
     * @param dayOfWeek 確認対象の曜日
     * @return 選択されている場合 true
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
