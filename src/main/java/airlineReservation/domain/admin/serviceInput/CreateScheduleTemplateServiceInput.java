package airlineReservation.domain.admin.serviceInput;

import airlineReservation.infra.dto.CreateScheduleTemplateRequestDaysOfWeek;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class CreateScheduleTemplateServiceInput {

    private String aircraftId;
    private String departureAirportId;
    private String arrivalAirportId;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime departureTime;

    private LocalTime arrivalTime;

    private Integer price;

    private CreateScheduleTemplateRequestDaysOfWeek daysOfWeek;

}
