package airlineReservation.domain.admin.serviceMapper;

import airlineReservation.domain.admin.serviceInput.CreateAircraftServiceInput;
import airlineReservation.domain.admin.serviceInput.SearchAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.CreateAircraftServiceOutput;
import airlineReservation.domain.admin.serviceOutput.SearchAircraftServiceOutput;
import airlineReservation.domain.dto.*;
import airlineReservation.domain.entity.Aircraft;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchAircraftServiceMapper {

    /**
     * Request ➔ ServiceInput
     */
    public SearchAircraftServiceInput toServiceInput(SearchAircraftRequest request) {
        if (request == null) return null;

        return SearchAircraftServiceInput.builder()
                .aircraftId(request.getAircraftId())
                .aircraftName(request.getAircraftName())
                .build();
    }

    /**
     * ServiceOutput ➔ OpenAPI Response DTO
     */
    public SearchAircraftResponse toResponse(SearchAircraftServiceOutput output) {
        if (output == null) return null;

        SearchAircraftResponse response = new SearchAircraftResponse();
        List<SearchAircraftResponseAircraftListInner> apiList = new ArrayList<>();

        if (output.getAircraftList() != null) {
            for (Aircraft entity : output.getAircraftList()) {
                // openapi-generator가 리스트 아이템용으로 자동 생성한 객체를 조립합니다.
                SearchAircraftResponseAircraftListInner item = new SearchAircraftResponseAircraftListInner();
                item.setAircraftId(entity.getAircraftId());
                item.setAircraftName(entity.getAircraftName());
                item.setRowCount(entity.getRowCount());
                item.setColumnCount(entity.getColumnCount());

                apiList.add(item);
            }
        }

        // OpenAPI 응답 객체에 최종 리스트를 바인딩합니다.
        response.setAircraftList(apiList);
        return response;
    }

}
