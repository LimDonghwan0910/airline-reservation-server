package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.DeleteAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.DeleteAircraftServiceOutput;
import airlineReservation.infra.entity.Aircraft;
import airlineReservation.infra.mapper.AircraftMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // ① Spring のビジネスロジック層コンポーネントとして登録する
@RequiredArgsConstructor // ② final のリポジトリを Spring が自動注入（DI）する
public class DeleteAircraftService {

    private final AircraftMapper aircraftMapper;

    /**
     * 航空機削除（論理削除）のビジネスロジック
     */
    @Transactional // ③ データを安全に DB へ保存し、エラー時はロールバックする
    public DeleteAircraftServiceOutput delete(DeleteAircraftServiceInput input) {

        Aircraft entity = new Aircraft();

        // 2. WHERE 句の基準となる Primary Key（aircraftId）を設定する
        entity.setAircraftId(input.getAircraftId());

        // 3. 変更対象フィールド（isDeleted）のみ true に設定する
        entity.setIsDeleted(true);

        // 4. MBG の中核メソッド呼び出し。PK を基準に null でないフィールドのみ UPDATE する
        // 実行 SQL: UPDATE aircrafts SET is_deleted = true WHERE aircraft_id = ?
        aircraftMapper.updateByPrimaryKeySelective(entity);

        // 5. コントローラへ返す成功結果（Output DTO）を組み立てる
        return DeleteAircraftServiceOutput.builder()
                .build();
    }
}
