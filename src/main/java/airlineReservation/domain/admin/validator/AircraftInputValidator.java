package airlineReservation.domain.admin.validator;

import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 航空機登録・更新時の入力値チェックを行うバリデータ。
 */
@Component
public class AircraftInputValidator {

    /**
     * 航空機ID・機種名・座席数の入力値を確認する。
     *
     * @param aircraftId 航空機ID
     * @param aircraftName 航空機機種名
     * @param rowCount 総行数
     * @param columnCount 総列数
     * @throws InvalidInputValueException 入力項目が誤っている場合
     */
    public void validate(String aircraftId, String aircraftName, Integer rowCount, Integer columnCount) {
        if (!StringUtils.hasText(aircraftId)) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "航空機IDを入力してください。");
        }
        if (!StringUtils.hasText(aircraftName)) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "航空機名を入力してください。");
        }
        if (rowCount == null) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "座席の行数を入力してください。");
        }
        if (columnCount == null) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "座席の列数を入力してください。");
        }
        if (rowCount < Const.SEAT_LAYOUT.MIN_ROWS || rowCount > Const.SEAT_LAYOUT.MAX_ROWS) {
            throw new InvalidInputValueException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "座席の行数は" + Const.SEAT_LAYOUT.MIN_ROWS + "〜" + Const.SEAT_LAYOUT.MAX_ROWS + "の範囲で入力してください。"
            );
        }
        if (columnCount < Const.SEAT_LAYOUT.MIN_COLS || columnCount > Const.SEAT_LAYOUT.MAX_COLS) {
            throw new InvalidInputValueException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "座席の列数は" + Const.SEAT_LAYOUT.MIN_COLS + "〜" + Const.SEAT_LAYOUT.MAX_COLS + "の範囲で入力してください。"
            );
        }
    }
}
