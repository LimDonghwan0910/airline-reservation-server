package airlineReservation.domain.admin.serviceMapper;

import airlineReservation.domain.admin.serviceInput.DeleteScheduleTemplateServiceInput;
import airlineReservation.domain.admin.serviceOutput.DeleteScheduleTemplateServiceOutput;
import airlineReservation.infra.dto.DeleteScheduleTemplateRequest;
import airlineReservation.infra.dto.DeleteScheduleTemplateResponse;
import org.springframework.stereotype.Component;

@Component
public class DeleteScheduleTemplateServiceMapper {

    public DeleteScheduleTemplateServiceInput toServiceInput(DeleteScheduleTemplateRequest request) {
        if (request == null) {
            return null;
        }

        return DeleteScheduleTemplateServiceInput.builder()
                .templateId(request.getTemplateId())
                .build();
    }

    public DeleteScheduleTemplateResponse toResponse(DeleteScheduleTemplateServiceOutput output) {
        DeleteScheduleTemplateResponse response = new DeleteScheduleTemplateResponse();
        response.setSuccess(true);
        return response;
    }
}
