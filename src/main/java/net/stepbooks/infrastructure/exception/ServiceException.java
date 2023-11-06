package net.stepbooks.infrastructure.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private final ErrorCode errorCode;

    public ServiceException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ServiceException(ErrorCode errorCode, String msg) {
        super(msg);
        errorCode.setMessage(msg);
        this.errorCode = errorCode;
    }

}
