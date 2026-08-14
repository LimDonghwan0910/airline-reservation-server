package airlineReservation.domain.admin.serviceMapper;

import airlineReservation.domain.admin.serviceInput.SearchAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.SearchAircraftServiceOutput;
import airlineReservation.infra.dto.SearchAircraftRequest;
import airlineReservation.infra.dto.SearchAircraftResponse;
import airlineReservation.infra.dto.SearchAircraftResponseAircraftListInner;
import airlineReservation.infra.entity.Aircraft;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchAircraftServiceMapper {

    public SearchAircraftServiceInput toServiceInput(SearchAircraftRequest request) {
        if (request == null) return null;

        return SearchAircraftServiceInput.builder()
                .aircraftId(request.getAircraftId())
                .aircraftName(request.getAircraftName())
                .build();
    }

    public SearchAircraftResponse toResponse(SearchAircraftServiceOutput output) {
        if (output == null) return null;

        SearchAircraftResponse response = new SearchAircraftResponse();
        List<SearchAircraftResponseAircraftListInner> apiList = new ArrayList<>();

        if (output.getAircraftList() != null) {
            for (Aircraft entity : output.getAircraftList()) {
                SearchAircraftResponseAircraftListInner item = new SearchAircraftResponseAircraftListInner();
                item.setAircraftId(entity.getAircraftId());
                item.setAircraftName(entity.getAircraftName());
                item.setRowCount(entity.getRowCount());
                item.setColumnCount(entity.getColumnCount());

                apiList.add(item);
            }
        }

        response.setAircraftList(apiList);
        return response;
    }

}
