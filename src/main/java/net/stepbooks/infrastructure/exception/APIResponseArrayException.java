package net.stepbooks.infrastructure.exception;

import lombok.Getter;
import net.stepbooks.infrastructure.model.APIResponse;

import java.util.List;

@Getter
public class APIResponseArrayException extends RuntimeException {

    private List<APIResponse> apiResponses;

    public APIResponseArrayException(List<APIResponse> apiResponses) {
        this.apiResponses = apiResponses;
    }
}
