package net.stepbooks.infrastructure.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String msg) {
        super(msg);
        errorCode.setMessage(msg);
        this.errorCode = errorCode;
    }

}
