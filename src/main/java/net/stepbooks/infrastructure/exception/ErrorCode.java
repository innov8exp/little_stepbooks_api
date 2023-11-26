package net.stepbooks.infrastructure.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    CONSTRAINT_VALIDATION_ERROR(HttpStatus.BAD_REQUEST.value(), "Constraint validation error"),

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
    REDIS_CONNECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Connect redis failed"),
    PRODUCT_NATURE_NOT_SUPPORT(HttpStatus.BAD_REQUEST.value(), "The product nature is not support"),
    ORDER_NATURE_NOT_SUPPORT(HttpStatus.BAD_REQUEST.value(), "The order nature is not support"),
    BOOK_SET_HAS_BEEN_USED(HttpStatus.BAD_REQUEST.value(), "The book set has been used"),
    PRODUCT_ON_SHELF_CANNOT_BE_DELETED(HttpStatus.BAD_REQUEST.value(),
            "The product on shelf cannot be deleted"),
    BOOK_HAS_COURSE(HttpStatus.BAD_REQUEST.value(), "The book has course, cannot be deleted"),
    BOOK_HAS_CHAPTER(HttpStatus.BAD_REQUEST.value(), "The book has chapter, cannot be deleted"),
    BOOK_HAS_BOOKSET(HttpStatus.BAD_REQUEST.value(), "The book has bind by bookset, cannot be deleted"),
    BOOK_SET_EXISTS_ERROR(HttpStatus.BAD_REQUEST.value(),
            "The book set has already exist, please input another one."),
    BOOK_SET_NOT_EXISTS_IN_ORDER_ERROR(HttpStatus.BAD_REQUEST.value(),
            "The book set is not exists in order, please check again."),
    BOOK_NOT_EXISTS_IN_ORDER_ERROR(HttpStatus.FORBIDDEN.value(), "The book is not exists in order, please check again."),
    ORDER_STATE_NOT_SUPPORT_REFUND(HttpStatus.BAD_REQUEST.value(), "The order state is not support refund"),
    REFUND_REQUEST_EXISTS(HttpStatus.BAD_REQUEST.value(), "The order has refund request, please check again."),
    VIRTUAL_ORDER_NOT_SUPPORT_REFUND(HttpStatus.BAD_REQUEST.value(), "The virtual order is not support refund"),
    ONLY_SELF_CAN_UPDATE(HttpStatus.FORBIDDEN.value(), "Only self can update"),
    COURSE_NEED_TO_PAY(HttpStatus.FORBIDDEN.value(), "The course need to pay"),
    COURSE_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "The course is not found"),
    ORDER_QUANTITY_IS_ZERO(HttpStatus.BAD_REQUEST.value(), "The order quantity is zero"),
    MEDIA_NOT_FOUND(HttpStatus.NO_CONTENT.value(), "The media is not found");


    private final int status;
    @Setter
    private String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
