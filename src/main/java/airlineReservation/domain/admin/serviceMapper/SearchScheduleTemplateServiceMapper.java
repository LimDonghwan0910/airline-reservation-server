package airlineReservation.domain.admin.serviceMapper;

import airlineReservation.domain.admin.serviceInput.SearchScheduleTemplateServiceInput;
import airlineReservation.domain.admin.serviceOutput.SearchScheduleTemplateServiceOutput;
import airlineReservation.infra.dto.CreateScheduleTemplateRequestDaysOfWeek;
import airlineReservation.infra.dto.SearchScheduleTemplateRequest;
import airlineReservation.infra.dto.SearchScheduleTemplateResponse;
import airlineReservation.infra.dto.SearchScheduleTemplateResponseScheduleTemplateListInner;
import airlineReservation.infra.entity.ScheduleTemplates;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchScheduleTemplateServiceMapper {

    public SearchScheduleTemplateServiceInput toServiceInput(SearchScheduleTemplateRequest request) {
        if (request == null) {
            return null;
        }

        return SearchScheduleTemplateServiceInput.builder()
                .aircraftId(request.getAircraftId())
                .build();
    }

    public SearchScheduleTemplateResponse toResponse(SearchScheduleTemplateServiceOutput output) {
        if (output == null) {
            return null;
        }

        SearchScheduleTemplateResponse response = new SearchScheduleTemplateResponse();
        List<SearchScheduleTemplateResponseScheduleTemplateListInner> apiList = new ArrayList<>();

        if (output.getScheduleTemplateList() != null) {
            for (ScheduleTemplates entity : output.getScheduleTemplateList()) {
                apiList.add(toListItem(entity));
            }
        }

        response.setScheduleTemplateList(apiList);
        return response;
    }

    private SearchScheduleTemplateResponseScheduleTemplateListInner toListItem(ScheduleTemplates entity) {
        SearchScheduleTemplateResponseScheduleTemplateListInner item =
                new SearchScheduleTemplateResponseScheduleTemplateListInner();

        item.setTemplateId(entity.getTemplateId());
        item.setAircraftId(entity.getAircraftId());
        item.setDepartureAirportId(entity.getDepartureAirportId());
        item.setArrivalAirportId(entity.getArrivalAirportId());
        item.setStartDate(entity.getStartDate());
        item.setEndDate(entity.getEndDate());
        item.setDepartureTime(entity.getDepartureTime());
        item.setArrivalTime(entity.getArrivalTime());
        item.setPrice(entity.getPrice());
        item.setDaysOfWeek(toDaysOfWeek(entity));

        return item;
    }

    private CreateScheduleTemplateRequestDaysOfWeek toDaysOfWeek(ScheduleTemplates entity) {
        CreateScheduleTemplateRequestDaysOfWeek daysOfWeek = new CreateScheduleTemplateRequestDaysOfWeek();
        daysOfWeek.setMon(Boolean.TRUE.equals(entity.getIsMonday()));
        daysOfWeek.setTue(Boolean.TRUE.equals(entity.getIsTuesday()));
        daysOfWeek.setWed(Boolean.TRUE.equals(entity.getIsWednesday()));
        daysOfWeek.setThu(Boolean.TRUE.equals(entity.getIsThursday()));
        daysOfWeek.setFri(Boolean.TRUE.equals(entity.getIsFriday()));
        daysOfWeek.setSat(Boolean.TRUE.equals(entity.getIsSaturday()));
        daysOfWeek.setSun(Boolean.TRUE.equals(entity.getIsSunday()));
        return daysOfWeek;
    }

}
