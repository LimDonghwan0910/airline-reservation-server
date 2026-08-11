package airlineReservation.global.exception;

public class ConflictException extends CustomException {

    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ConflictException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
