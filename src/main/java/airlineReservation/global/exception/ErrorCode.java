package airlineReservation.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "SE0001", "入力値が無効です。"),
    INPUT_NOT_FOUND(HttpStatus.BAD_REQUEST, "SE0002", "値を入力してください。"),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "SE0003", "ユーザーが見つかりません。"),

    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "SE0004", "既に使用されているメールアドレスです。"),
    DUPLICATE_SEAT(HttpStatus.CONFLICT, "SE0005", "選択した座席は既に予約されています。"),
    BOOKING_NOT_FOUND(HttpStatus.NOT_FOUND, "SE0006", "予約が見つかりません。"),
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "SE0007", "座席が見つかりません。"),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SE0008", "運航スケジュールが見つかりません。"),
    AIRCRAFT_NOT_FOUND(HttpStatus.NOT_FOUND, "SE0009", "航空機が見つかりません。"),
    AIRPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "SE0010", "空港が見つかりません。"),
    TEMPLATE_NOT_FOUND(HttpStatus.NOT_FOUND, "SE0011", "定期運航テンプレートが見つかりません。"),
    BOOKING_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "SE0012", "既にキャンセルされた予約です。"),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "SE0013", "メールアドレスまたはパスワードが正しくありません。"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "SE0014", "会員が見つかりません。"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "SE0015", "認証が必要です。"),
    ACTIVE_BOOKING_EXISTS(HttpStatus.CONFLICT, "SE0016", "有効な予約があるため退会できません。"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SE9999", "サーバー内部エラーが発生しました。");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
