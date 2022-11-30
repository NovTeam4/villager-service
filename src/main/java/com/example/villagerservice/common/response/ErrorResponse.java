package com.example.villagerservice.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final String errorCode;
    private final String errorMessage;
    private Map<String, String> validation = null;

    @Builder
    private ErrorResponse(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public void addValidError(String fieldName, String errorMessage) {
        if (validation == null) {
            validation = new HashMap<>();
        }
        this.validation.put(fieldName, errorMessage);
    }
}
