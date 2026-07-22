package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.CreateAircraftServiceInput;
import airlineReservation.domain.admin.serviceInput.SearchAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.CreateAircraftServiceOutput;
import airlineReservation.domain.admin.serviceOutput.SearchAircraftServiceOutput;
import airlineReservation.domain.entity.Aircraft;
import airlineReservation.domain.entity.AircraftExample;
import airlineReservation.infra.mapper.AircraftMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service // ① 스프링의 비즈니스 로직 레이어 컴포넌트로 등록합니다.
@RequiredArgsConstructor // ② final이 붙은 리포지토리를 스프링이 자동으로 주입(DI)해 줍니다.
public class SearchAircraftService {

    private final AircraftMapper aircraftMapper;

    /**
     * 항공기 조건 검색 비즈니스 로직
     */
    public SearchAircraftServiceOutput search(SearchAircraftServiceInput input) {

        // 2. MBG의 핵심 부품인 Example 객체를 생성합니다. (동적 WHERE 절을 만들어 줍니다)
        AircraftExample example = new AircraftExample();
        AircraftExample.Criteria criteria = example.createCriteria();

        // 3. 삭제되지 않은(is_deleted = false) 비행기만 조회하도록 기본 조건을 겁니다.
        criteria.andIsDeletedEqualTo(false);

        // 4. 항공기 ID 조건이 들어온 경우 (동적 쿼리)
        if (StringUtils.hasText(input.getAircraftId())) {
            // 완전히 일치하는 ID를 찾거나, 필요시 Like 검색으로 변경 가능합니다.
            criteria.andAircraftIdEqualTo(input.getAircraftId());
        }

        // 5. 항공기 이름 조건이 들어온 경우 (동적 쿼리 - 포함 검색 Like 적용)
        if (StringUtils.hasText(input.getAircraftName())) {
            // %이름% 형태로 매핑하여 부분 일치 검색이 가능하게 만듭니다.
            criteria.andAircraftNameLike("%" + input.getAircraftName() + "%");
        }

        // 정렬 조건 추가 (예: 최신순 또는 ID순으로 정렬하고 싶을 때)
        example.setOrderByClause("aircraft_id ASC");

        // 6. MBG가 만들어 준 selectByExample 메서드로 DB 조회를 실행합니다.
        List<Aircraft> aircraftList = aircraftMapper.selectByExample(example);

        // 7. 서비스 출력 DTO에 담아서 컨트롤러로 반환합니다.
        return SearchAircraftServiceOutput.builder()
                .aircraftList(aircraftList)
                .build();
    }
}