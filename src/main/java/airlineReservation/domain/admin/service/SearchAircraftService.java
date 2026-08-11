package airlineReservation.domain.admin.service;

import airlineReservation.domain.admin.serviceInput.SearchAircraftServiceInput;
import airlineReservation.domain.admin.serviceOutput.SearchAircraftServiceOutput;
import airlineReservation.infra.entity.Aircraft;
import airlineReservation.infra.entity.AircraftExample;
import airlineReservation.infra.mapper.AircraftMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service // ① Spring のビジネスロジック層コンポーネントとして登録する
@RequiredArgsConstructor // ② final のリポジトリを Spring が自動注入（DI）する
public class SearchAircraftService {

    private final AircraftMapper aircraftMapper;

    /**
     * 航空機条件検索のビジネスロジック
     */
    public SearchAircraftServiceOutput search(SearchAircraftServiceInput input) {

        // 2. MBG の中核部品である Example オブジェクトを生成する（動的 WHERE 句を作る）
        AircraftExample example = new AircraftExample();
        AircraftExample.Criteria criteria = example.createCriteria();

        // 3. 削除されていない（is_deleted = false）航空機のみを検索する基本条件を付ける
        criteria.andIsDeletedEqualTo(false);

        // 4. 航空機 ID 条件が渡された場合（動的クエリ）
        if (StringUtils.hasText(input.getAircraftId())) {
            // 完全一致の ID を検索する。必要なら Like 検索へ変更可能
            criteria.andAircraftIdEqualTo(input.getAircraftId());
        }

        // 5. 航空機名条件が渡された場合（動的クエリ - 部分一致 Like）
        if (StringUtils.hasText(input.getAircraftName())) {
            // %名前% 形式でマッピングし、部分一致検索を可能にする
            criteria.andAircraftNameLike("%" + input.getAircraftName() + "%");
        }

        // ソート条件を追加（例: 最新順または ID 順で並べたいとき）
        example.setOrderByClause("aircraft_id ASC");

        // 6. MBG が生成した selectByExample メソッドで DB 検索を実行する
        List<Aircraft> aircraftList = aircraftMapper.selectByExample(example);

        // 7. サービス出力 DTO に詰めてコントローラへ返す
        return SearchAircraftServiceOutput.builder()
                .aircraftList(aircraftList)
                .build();
    }
}
