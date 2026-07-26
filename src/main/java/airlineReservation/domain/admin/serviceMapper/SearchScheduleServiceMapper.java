package airlineReservation.domain.admin.serviceMapper;

import airlineReservation.domain.admin.serviceInput.SearchScheduleServiceInput;
import airlineReservation.domain.admin.serviceOutput.SearchScheduleServiceOutput;
import airlineReservation.infra.dto.SearchScheduleRequest;
import airlineReservation.infra.dto.SearchScheduleResponse;
import airlineReservation.infra.dto.SearchScheduleResponseScheduleListInner;
import airlineReservation.infra.entity.Schedule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchScheduleServiceMapper {

    public SearchScheduleServiceInput toServiceInput(SearchScheduleRequest request) {
        if (request == null) {
            return null;
        }

        return SearchScheduleServiceInput.builder()
                .aircraftId(request.getAircraftId())
                .departureAirportId(request.getDepartureAirportId())
                .arrivalAirportId(request.getArrivalAirportId())
                .departureDate(request.getDepartureDate())
                .build();
    }

    public SearchScheduleResponse toResponse(SearchScheduleServiceOutput output) {
        if (output == null) {
            return null;
        }

        SearchScheduleResponse response = new SearchScheduleResponse();
        List<SearchScheduleResponseScheduleListInner> apiList = new ArrayList<>();

        if (output.getScheduleList() != null) {
            for (Schedule entity : output.getScheduleList()) {
                apiList.add(toListItem(entity));
            }
        }

        response.setScheduleList(apiList);
        return response;
    }

    private SearchScheduleResponseScheduleListInner toListItem(Schedule entity) {
        SearchScheduleResponseScheduleListInner item = new SearchScheduleResponseScheduleListInner();

        item.setScheduleId(entity.getScheduleId());
        item.setTemplateId(entity.getTemplateId());
        item.setAircraftId(entity.getAircraftId());
        item.setDepartureAirportId(entity.getDepartureAirportId());
        item.setArrivalAirportId(entity.getArrivalAirportId());
        item.setDepartureDatetime(entity.getDepartureDatetime());
        item.setArrivalDatetime(entity.getArrivalDatetime());
        item.setStatus(entity.getStatus());

        return item;
    }
}
