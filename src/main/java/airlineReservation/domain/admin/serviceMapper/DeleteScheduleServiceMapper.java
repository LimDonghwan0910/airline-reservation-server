package airlineReservation.domain.admin.serviceMapper;

import airlineReservation.domain.admin.serviceInput.DeleteScheduleServiceInput;
import airlineReservation.domain.admin.serviceOutput.DeleteScheduleServiceOutput;
import airlineReservation.infra.dto.DeleteScheduleRequest;
import airlineReservation.infra.dto.DeleteScheduleResponse;
import org.springframework.stereotype.Component;

@Component
public class DeleteScheduleServiceMapper {

    public DeleteScheduleServiceInput toServiceInput(DeleteScheduleRequest request) {
        if (request == null) {
            return null;
        }

        return DeleteScheduleServiceInput.builder()
                .scheduleId(request.getScheduleId())
                .build();
    }

    public DeleteScheduleResponse toResponse(DeleteScheduleServiceOutput output) {
        DeleteScheduleResponse response = new DeleteScheduleResponse();
        response.setSuccess(true);
        return response;
    }
}
