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
 * 정기 운항 템플릿 생성 서비스.
 *
 * [처리 흐름]
 * 1) schedule_templates 테이블에 "정기 운항 규칙(템플릿)" 1건 저장
 * 2) 시작일~종료일 사이 날짜를 하루씩 순회
 * 3) 그날 요일이 선택된 요일(mon~sun)이면 schedules 테이블에 실제 운항 1건 저장
 *
 * 예) 2026-07-01 ~ 2026-07-31, 화/목 선택, 09:00 출발
 *   → schedule_templates 1건 + 해당 기간의 모든 화요일/목요일 schedules N건 생성
 */
@Service
@RequiredArgsConstructor
public class CreateScheduleTemplateService {

    /** schedules.status 기본값: 예정(SCHEDULED) */
    private static final String STATUS_SCHEDULED = "SCHEDULED";

    private final ScheduleTemplatesMapper scheduleTemplatesMapper;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleReferenceValidator scheduleReferenceValidator;
    private final ScheduleSeatProvisioningService scheduleSeatProvisioningService;

    /**
     * 정기 운항 템플릿 + 실제 스케줄 일괄 생성.
     * @Transactional: 템플릿/스케줄 중 하나라도 실패하면 전체 롤백
     */
    @Transactional
    public CreateScheduleTemplateServiceOutput create(CreateScheduleTemplateServiceInput input) {
        // 1. 항공기·공항이 DB에 존재하는지 검증 (없으면 예외 발생)
        scheduleReferenceValidator.validateAircraft(input.getAircraftId());
        scheduleReferenceValidator.validateAirport(input.getDepartureAirportId());
        scheduleReferenceValidator.validateAirport(input.getArrivalAirportId());

        CreateScheduleTemplateRequestDaysOfWeek daysOfWeek = input.getDaysOfWeek();

        // 2. schedule_templates에 템플릿 1건 저장 → 생성된 template_id 반환
        Integer templateId = insertScheduleTemplate(input, daysOfWeek);

        // 3. 시작일부터 종료일까지 하루씩 순회하며, 선택된 요일이면 schedules에 저장
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
     * schedule_templates(정기 운항 템플릿) 1건 INSERT.
     * 템플릿은 "언제, 어떤 요일, 몇 시에 운항하는지"의 규칙만 저장한다.
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

        // API의 daysOfWeek(mon~sun) → DB 컬럼 is_monday ~ is_sunday 로 변환
        template.setIsMonday(isDaySelected(daysOfWeek, DayOfWeek.MONDAY));
        template.setIsTuesday(isDaySelected(daysOfWeek, DayOfWeek.TUESDAY));
        template.setIsWednesday(isDaySelected(daysOfWeek, DayOfWeek.WEDNESDAY));
        template.setIsThursday(isDaySelected(daysOfWeek, DayOfWeek.THURSDAY));
        template.setIsFriday(isDaySelected(daysOfWeek, DayOfWeek.FRIDAY));
        template.setIsSaturday(isDaySelected(daysOfWeek, DayOfWeek.SATURDAY));
        template.setIsSunday(isDaySelected(daysOfWeek, DayOfWeek.SUNDAY));

        scheduleTemplatesMapper.insertSelective(template);

        // insertSelective 후 templateId가 entity에 자동 세팅되면 바로 반환
        if (template.getTemplateId() != null) {
            return template.getTemplateId();
        }

        // MyBatis 설정에 따라 PK가 entity에 안 채워질 수 있어, 방금 넣은 데이터로 재조회
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
                .orElseThrow(() -> new IllegalStateException("schedule_templates template_id 생성에 실패했습니다."));
    }

    /**
     * schedules(실제 운항 일정) 1건 INSERT.
     *
     * @param templateId  연결된 템플릿 PK (schedule_templates.template_id)
     * @param flightDate  이 스케줄의 운항 날짜 (루프에서 하루씩 넘어오는 값)
     */
    private void insertSchedule(
            Integer templateId,
            CreateScheduleTemplateServiceInput input,
            LocalDate flightDate) {
        // 날짜 + 시각 → departure_datetime (예: 2026-07-22 09:00:00)
        LocalDateTime departureDateTime = LocalDateTime.of(flightDate, input.getDepartureTime());

        // 도착 시각이 출발 시각보다 이르면 익일 도착 (예: 23:00 출발 → 01:00 다음날 도착)
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
     * insertSelective 후 schedule_id를 반환한다.
     * MyBatis 설정에 따라 PK가 entity에 채워지지 않을 수 있어 재조회한다.
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
                .orElseThrow(() -> new IllegalStateException("schedules schedule_id 생성에 실패했습니다."));
    }

    /** 특정 날짜의 요일이 사용자가 선택한 요일인지 확인 */
    private boolean isSelectedDayOfWeek(DayOfWeek dayOfWeek, CreateScheduleTemplateRequestDaysOfWeek daysOfWeek) {
        return isDaySelected(daysOfWeek, dayOfWeek);
    }

    /**
     * daysOfWeek 객체에서 해당 요일 flag(mon~sun)가 true인지 확인.
     * null이거나 false면 운항하지 않는 요일로 간주.
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
