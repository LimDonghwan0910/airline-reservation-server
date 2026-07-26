package airlineReservation.domain.admin.serviceInput;

import airlineReservation.infra.dto.CreateScheduleTemplateRequestDaysOfWeek;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class CreateScheduleTemplateServiceInput {

    // 空港機コード
    private String aircraftId;
    // 出発空港コード
    private String departureAirportId;
    // 到着空港コード
    private String arrivalAirportId;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime departureTime;

    private LocalTime arrivalTime;

    private Integer price;

    private CreateScheduleTemplateRequestDaysOfWeek daysOfWeek;

}
