package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.CreateAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.CreateAircraftServiceOutput;
import airlineReservation.domain.admin.validator.AircraftInputValidator;
import airlineReservation.global.exception.DuplicateException;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.infra.entity.Aircraft;
import airlineReservation.infra.mapper.AircraftMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 航空機登録処理を行うサービス。
 */
@Service
@RequiredArgsConstructor
public class CreateAircraftService {

    private final AircraftMapper aircraftMapper;
    private final AircraftInputValidator aircraftInputValidator;

    /**
     * 航空機を新規登録する。
     *
     * @param input
     * @return serviceOutput
     * @throws InvalidInputValueException 入力項目が誤っている場合
     * @throws DuplicateException 航空機IDが既に登録されている場合
     */
    @Transactional
    public CreateAircraftServiceOutput create(CreateAircraftServiceInput input) {
        aircraftInputValidator.validate(
                input.getAircraftId(),
                input.getAircraftName(),
                input.getRowCount(),
                input.getColumnCount()
        );
        ensureAircraftIdAvailable(input.getAircraftId());

        Aircraft entity = new Aircraft();
        entity.setAircraftId(input.getAircraftId());
        entity.setAircraftName(input.getAircraftName());
        entity.setRowCount(input.getRowCount());
        entity.setColumnCount(input.getColumnCount());
        entity.setIsDeleted(false);

        aircraftMapper.insertSelective(entity);

        return CreateAircraftServiceOutput.builder()
                .build();
    }

    /**
     * 航空機IDの重複チェックを行う。
     *
     * @param aircraftId チェック対象の航空機ID
     * @throws DuplicateException 既に同一IDが登録されている場合
     */
    private void ensureAircraftIdAvailable(String aircraftId) {
        Aircraft existing = aircraftMapper.selectByPrimaryKey(aircraftId);
        if (existing != null) {
            throw new DuplicateException(ErrorCode.DUPLICATE_AIRCRAFT_ID);
        }
    }
}
