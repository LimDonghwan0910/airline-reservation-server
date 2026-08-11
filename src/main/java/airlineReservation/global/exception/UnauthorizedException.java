package airlineReservation.global.exception;

public class UnauthorizedException extends CustomException {

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnauthorizedException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
