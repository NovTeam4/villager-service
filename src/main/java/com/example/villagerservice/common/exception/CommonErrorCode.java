package com.example.villagerservice.common.exception;

import com.example.villagerservice.common.response.ErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode {

    DATA_INVALID_ERROR("INFO-100", "유효성 검사에 실패하였습니다.");

    private final String errorCode;
    private final String errorMessage;

    public static ErrorResponse createErrorResponse(CommonErrorCode errorCode) {
        return ErrorResponse.builder()
                .errorCode(errorCode.getErrorCode())
                .errorMessage(errorCode.getErrorMessage())
                .build();
    }
}
