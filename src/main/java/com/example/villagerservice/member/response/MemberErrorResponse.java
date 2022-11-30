package com.example.villagerservice.member.response;

import com.example.villagerservice.member.exception.MemberErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberErrorResponse {
    private final String errorCode;
    private final String errorMessage;
    private MultiValueMap<String, String> validation = null;

    @Builder
    private MemberErrorResponse(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }


    public void addMemberValidError(String fieldName, String errorMessage) {
        if(validation == null) {
            validation = new LinkedMultiValueMap<>();
        }
        this.validation.add(fieldName, errorMessage);
    }
}