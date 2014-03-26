package <%=packageName%>.web.rest;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 *
 */
@Data
public class RestError {
    private final LocalDateTime timeStamp = LocalDateTime.now();
    private final String error;
    private Integer status;
    private String message;

    RestError(ErrorCode error) {
        this.error = error.message;
        this.message = error.message;
        this.status = error.status;
    }

    public static final RestError NOT_FOUND_ERROR = new RestError(ErrorCode.NOT_FOUND);
    public static final RestError INVALID_ERROR = new RestError(ErrorCode.INVALID);
    public static final RestError UNAUTHORIZED_ACCESS_ERROR = new RestError(ErrorCode.UNAUTHORIZED_ACCESS);
    public static final RestError DUPLICATE_ERROR = new RestError(ErrorCode.DUPLICATE);
    public static final RestError METHOD_NOT_ALLOWED_ERROR = new RestError(ErrorCode.METHOD_NOT_ALLOWED);

    static enum ErrorCode {
        NOT_FOUND("Not found", HttpStatus.NOT_FOUND.value()),
        INVALID("Invalid", HttpStatus.BAD_REQUEST.value()),
        UNAUTHORIZED_ACCESS("Unauthorized", HttpStatus.UNAUTHORIZED.value()),
        DUPLICATE("Duplicate", HttpStatus.CONFLICT.value()),
        METHOD_NOT_ALLOWED("Method not allowed", HttpStatus.METHOD_NOT_ALLOWED.value());

        final String message;
        final Integer status;

        ErrorCode(String message, Integer status) {
            this.message = message;
            this.status = status;
        }
    }
}
