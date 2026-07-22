package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.CreateAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.CreateAircraftServiceOutput;
import airlineReservation.domain.entity.Aircraft;
import airlineReservation.infra.mapper.AircraftMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // ① 스프링의 비즈니스 로직 레이어 컴포넌트로 등록합니다.
@RequiredArgsConstructor // ② final이 붙은 리포지토리를 스프링이 자동으로 주입(DI)해 줍니다.
public class CreateAircraftService {

    private final AircraftMapper aircraftMapper;

    /**
     * 항공기 생성 비즈니스 로직
     */
    @Transactional // ③ 데이터가 안전하게 DB에 저장되거나 오류 시 롤백되도록 트랜잭션을 걸어줍니다.
    public CreateAircraftServiceOutput create(CreateAircraftServiceInput input) {

        Aircraft entity = new Aircraft();
        entity.setAircraftId(input.getAircraftId());
        entity.setAircraftName(input.getAircraftName());
        entity.setRowCount(input.getRowCount());
        entity.setColumnCount(input.getColumnCount());
        entity.setIsDeleted(false);

        aircraftMapper.insertSelective(entity);

        // 4. 컨트롤러로 돌려줄 성공 결과(Output DTO) 조립
        return CreateAircraftServiceOutput.builder()
                .build();
    }
}