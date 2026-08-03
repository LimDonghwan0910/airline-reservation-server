package airlineReservation.domain.common.serviceMapper;

import airlineReservation.domain.common.serviceInput.GetSeatServiceInput;
import airlineReservation.domain.common.serviceOutput.GetSeatServiceOutput;
import airlineReservation.infra.dto.GetSeatsRequest;
import airlineReservation.infra.dto.GetSeatsResponse;
import airlineReservation.infra.dto.GetSeatsResponseSeatListInner;
import airlineReservation.infra.entity.ScheduleSeat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetSeatServiceMapper {

    public GetSeatServiceInput toServiceInput(GetSeatsRequest request) {
        if (request == null) {
            return null;
        }

        return GetSeatServiceInput.builder()
                .scheduleId(request.getScheduleId())
                .build();
    }

    public GetSeatsResponse toResponse(GetSeatServiceOutput output) {
        if (output == null) {
            return null;
        }

        GetSeatsResponse response = new GetSeatsResponse();
        response.setScheduleId(output.getScheduleId());

        List<GetSeatsResponseSeatListInner> seatList = new ArrayList<>();

        if (output.getSeatList() != null) {
            for (ScheduleSeat seat : output.getSeatList()) {
                seatList.add(addListItem(seat));
            }
        }

        response.setSeatList(seatList);
        return response;
    }

    private GetSeatsResponseSeatListInner addListItem(ScheduleSeat seat) {
        GetSeatsResponseSeatListInner item = new GetSeatsResponseSeatListInner();

        item.setScheduledSeatNo(seat.getScheduledSeatNo());
        item.setSeatName(seat.getSeatName());
        item.setStatus(seat.getStatus());

        return item;
    }
}
