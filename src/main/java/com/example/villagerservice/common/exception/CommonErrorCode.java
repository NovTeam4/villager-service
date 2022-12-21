package com.example.villagerservice.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode {
    SERVER_INVALID_ERROR("INFO-000", "INTERNAL SERVER ERROR"),
    DATA_INVALID_ERROR("INFO-100", "유효성 검사에 실패하였습니다."),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED_ERROR("INFO-101", "요청 URL을 확인해주세요."),
    HTTP_MESSAGE_NOT_READABLE("INFO-102" , "요청 인자를 확인해주세요."),
    MAX_UPLOAD_SIZE_EXCEEDED("INFO-103" , "파일 최대 사이즈(10MB)를 초과하였습니다.")

    ;

    private final String errorCode;
    private final String errorMessage;

    public static ErrorResponse createErrorResponse(CommonErrorCode errorCode) {
        return ErrorResponse.builder()
                .errorCode(errorCode.getErrorCode())
                .errorMessage(errorCode.getErrorMessage())
                .build();
    }
}
