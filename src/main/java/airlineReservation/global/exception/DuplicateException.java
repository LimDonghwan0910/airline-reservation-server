package airlineReservation.global.exception;

public class DuplicateException extends CustomException {

    public DuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DuplicateException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
