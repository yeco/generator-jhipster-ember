package <%=packageName%>.web.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Data
public class RestError {
    private final DateTime timeStamp = new DateTime();
    private final ErrorCode error;
    private Integer status;
    private List<String> messages = new ArrayList<>();

    RestError(ErrorCode error) {
        this.error = error;
        this.messages.add(error.message);
        this.status = error.status;
    }

    @JsonIgnore
    public String getDefaultMessage() {
        return this.messages.get(0);
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public void overrideDefaultMessage(String message) {
        this.messages.clear();
        addMessage(message);
    }

    public static final RestError NOT_FOUND_ERROR = new RestError(ErrorCode.NOT_FOUND);
    public static final RestError INVALID_ERROR = new RestError(ErrorCode.INVALID);
    public static final RestError UNAUTHORIZED_ACCESS = new RestError(ErrorCode.UNAUTHORIZED_ACCESS);
    public static final RestError DUPLICATE_ERROR = new RestError(ErrorCode.DUPLICATE);

    static enum ErrorCode {
        NOT_FOUND("Not found", HttpStatus.NOT_FOUND.value()),
        INVALID("Invalid", HttpStatus.BAD_REQUEST.value()),
        UNAUTHORIZED_ACCESS("Unauthorized", HttpStatus.UNAUTHORIZED.value()),
        DUPLICATE("Duplicate", HttpStatus.CONFLICT.value());

        final String message;
        final Integer status;

        ErrorCode(String message, Integer status) {
            this.message = message;
            this.status = status;
        }
    }
}
