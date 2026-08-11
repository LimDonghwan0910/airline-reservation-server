package airlineReservation.global.exception;

import airlineReservation.infra.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * CustomExceptionは400番台処理
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        log.warn("CustomException発生: Code={}, Message={}", errorCode.getCode(), e.getMessage());

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(createErrorResponse(errorCode.getCode(), e.getMessage()));
    }

    /**
     * @Valid または @Validated 検証失敗時、Bad Request処理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("Validation Exception 発生: {}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());

        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(ErrorCode.INPUT_NOT_FOUND.getCode(), errorMessage));
    }

    /**
     * CustomException以外の例外はInternal Server Error(500)処理
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unhandled Exception 発生: ", e);

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse(errorCode.getCode(), errorCode.getMessage()));
    }

    /**
     * エラーレスポンス生成
     *
     * @param errorCode, message
     * @return ErrorResponse
     */
    private ErrorResponse createErrorResponse(String errorCode, String message) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(errorCode);
        response.setMessage(message);
        return response;
    }


}
