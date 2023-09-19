package co.botechservices.novlnovl.infrastructure.exception;

import co.botechservices.novlnovl.infrastructure.model.APIResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class APIResponseArrayException extends RuntimeException {

    private List<APIResponse> apiResponses;

    public APIResponseArrayException(List<APIResponse> apiResponses) {
        this.apiResponses = apiResponses;
    }
}
