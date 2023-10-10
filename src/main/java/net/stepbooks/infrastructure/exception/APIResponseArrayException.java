package net.stepbooks.infrastructure.exception;

import net.stepbooks.infrastructure.model.APIResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class APIResponseArrayException extends RuntimeException {

    private List<APIResponse> apiResponses;

    public APIResponseArrayException(List<APIResponse> apiResponses) {
        this.apiResponses = apiResponses;
    }
}
