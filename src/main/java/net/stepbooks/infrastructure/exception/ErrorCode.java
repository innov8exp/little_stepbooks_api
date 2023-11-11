package net.stepbooks.infrastructure.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Cannot found the order"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Cannot found the user"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "The request param is not valid"),
    AUTH_ERROR(HttpStatus.UNAUTHORIZED.value(), "authenticated error"),
    EMAIL_EXISTS_ERROR(HttpStatus.BAD_REQUEST.value(), "The email has already exist, please input another one."),
    FILETYPE_ERROR(HttpStatus.BAD_REQUEST.value(), "file type validation error"),
    DATABASE_OPERATOR_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "database operator occur error"),
    PARSE_JSON_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "parse json occur error"),
    UPLOAD_FILE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Upload file to s3 failed"),
    S3_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Connect S3 failed"),

    PRODUCT_NOT_EXISTS(HttpStatus.BAD_REQUEST.value(), "The product is already unshelve"),
    STOCK_NOT_EXISTS(HttpStatus.BAD_REQUEST.value(), "The product is out of stock"),
    STOCK_NOT_ENOUGH(HttpStatus.BAD_REQUEST.value(), "The product is out of stock"),

    LOCK_STOCK_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lock stock failed"),

    REDUCE_BALANCE_FAILED(HttpStatus.BAD_REQUEST.value(),
            "Reduce coin failed, insufficient coin balance, please recharge"),
    SCHEDULE_TASK_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Schedule task occur error"),
    REDIS_CONNECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Connect redis failed");

    private final int status;
    @Setter
    private String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
