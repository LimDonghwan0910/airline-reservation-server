package airlineReservation.global.exception;

public class InvalidInputValueException extends CustomException {

    public InvalidInputValueException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidInputValueException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
