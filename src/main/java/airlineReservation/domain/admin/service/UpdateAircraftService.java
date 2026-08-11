package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.UpdateAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.UpdateAircraftServiceOutput;
import airlineReservation.infra.entity.Aircraft;
import airlineReservation.infra.mapper.AircraftMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateAircraftService {

    private final AircraftMapper aircraftMapper;

    /**
     * 航空機更新のビジネスロジック
     */
    @Transactional
    public UpdateAircraftServiceOutput update(UpdateAircraftServiceInput input) {

        Aircraft entity = new Aircraft();

        entity.setAircraftId(input.getAircraftId());

        entity.setAircraftName(input.getAircraftName());
        entity.setRowCount(input.getRowCount());
        entity.setColumnCount(input.getColumnCount());

        aircraftMapper.updateByPrimaryKeySelective(entity);

        return UpdateAircraftServiceOutput.builder()
                .build();
    }
}