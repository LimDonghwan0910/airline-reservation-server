package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.CreateAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.CreateAircraftServiceOutput;
import airlineReservation.infra.entity.Aircraft;
import airlineReservation.infra.mapper.AircraftMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // ① Spring のビジネスロジック層コンポーネントとして登録する
@RequiredArgsConstructor // ② final のリポジトリを Spring が自動注入（DI）する
public class CreateAircraftService {

    private final AircraftMapper aircraftMapper;

    /**
     * 航空機作成のビジネスロジック
     */
    @Transactional // ③ データを安全に DB へ保存し、エラー時はロールバックする
    public CreateAircraftServiceOutput create(CreateAircraftServiceInput input) {

        Aircraft entity = new Aircraft();
        entity.setAircraftId(input.getAircraftId());
        entity.setAircraftName(input.getAircraftName());
        entity.setRowCount(input.getRowCount());
        entity.setColumnCount(input.getColumnCount());
        entity.setIsDeleted(false);

        aircraftMapper.insertSelective(entity);

        // 4. コントローラへ返す成功結果（Output DTO）を組み立てる
        return CreateAircraftServiceOutput.builder()
                .build();
    }
}
