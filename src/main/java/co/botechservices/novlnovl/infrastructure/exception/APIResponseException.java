package co.botechservices.novlnovl.infrastructure.exception;

import lombok.Getter;

@Getter
public class APIResponseException extends RuntimeException {

    private final int statusCode;

    public APIResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
