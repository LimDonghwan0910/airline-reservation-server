package airlineReservation.domain.admin.serviceMapper;

import airlineReservation.domain.admin.serviceInput.CreateScheduleTemplateServiceInput;
import airlineReservation.domain.admin.serviceOutput.CreateScheduleTemplateServiceOutput;
import airlineReservation.infra.dto.CreateScheduleTemplateRequest;
import airlineReservation.infra.dto.CreateScheduleTemplateResponse;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class CreateScheduleTemplateServiceMapper {

    public CreateScheduleTemplateServiceInput toServiceInput(CreateScheduleTemplateRequest request) {
        if (request == null) {
            return null;
        }

        return CreateScheduleTemplateServiceInput.builder()
                .aircraftId(request.getAircraftId())
                .departureAirportId(request.getDepartureAirportId())
                .arrivalAirportId(request.getArrivalAirportId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .departureTime(request.getDepartureTime())
                .arrivalTime(request.getArrivalTime())
                .price(request.getPrice())
                .daysOfWeek(request.getDaysOfWeek())
                .build();
    }

    public CreateScheduleTemplateResponse toResponse(CreateScheduleTemplateServiceOutput output) {

        return new CreateScheduleTemplateResponse();
    }

}
