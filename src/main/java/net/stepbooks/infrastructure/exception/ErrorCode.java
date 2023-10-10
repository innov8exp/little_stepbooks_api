package net.stepbooks.infrastructure.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Cannot found the user"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "The request param is not valid"),
    AUTH_ERROR(HttpStatus.UNAUTHORIZED.value(), "authenticated error"),
    EMAIL_EXISTS_ERROR(HttpStatus.BAD_REQUEST.value(), "The email has already exist, please input another one."),
    FILETYPE_ERROR(HttpStatus.BAD_REQUEST.value(), "file type validation error"),
    DATABASE_OPERATOR_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "database operator occur error"),
    PARSE_JSON_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "parse json occur error"),
    UPLOAD_FILE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Upload file to s3 failed"),
    REDUCE_BALANCE_FAILED(HttpStatus.BAD_REQUEST.value(),
            "Reduce coin failed, insufficient coin balance, please recharge");

    private final int status;
    @Setter
    private String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
