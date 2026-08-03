package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.CreateAircraftServiceInput;
import airlineReservation.domain.admin.serviceInput.DeleteAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.CreateAircraftServiceOutput;
import airlineReservation.domain.admin.serviceOutput.DeleteAircraftServiceOutput;
import airlineReservation.infra.entity.Aircraft;
import airlineReservation.infra.mapper.AircraftMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // ① 스프링의 비즈니스 로직 레이어 컴포넌트로 등록합니다.
@RequiredArgsConstructor // ② final이 붙은 리포지토리를 스프링이 자동으로 주입(DI)해 줍니다.
public class DeleteAircraftService {

    private final AircraftMapper aircraftMapper;

    /**
     * 항공기 생성 비즈니스 로직
     */
    @Transactional // ③ 데이터가 안전하게 DB에 저장되거나 오류 시 롤백되도록 트랜잭션을 걸어줍니다.
    public DeleteAircraftServiceOutput delete(DeleteAircraftServiceInput input) {

        Aircraft entity = new Aircraft();

        // 2. WHERE 절의 기준이 될 Primary Key(aircraftId)를 세팅합니다.
        entity.setAircraftId(input.getAircraftId());

        // 3. 변경하고 싶은 타겟 필드(isDeleted)만 true로 설정합니다.
        entity.setIsDeleted(true);

        // 4. MBG 핵심 메서드 호출! PK를 기준으로 null이 아닌 필드만 콕 집어서 UPDATE 해줍니다.
        // 실행되는 SQL: UPDATE aircrafts SET is_deleted = true WHERE aircraft_id = ?
        aircraftMapper.updateByPrimaryKeySelective(entity);

        // 4. 컨트롤러로 돌려줄 성공 결과(Output DTO) 조립
        return DeleteAircraftServiceOutput.builder()
                .build();
    }
}