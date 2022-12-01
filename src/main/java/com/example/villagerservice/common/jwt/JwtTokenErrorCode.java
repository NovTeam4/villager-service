package com.example.villagerservice.common.jwt;

import com.example.villagerservice.common.exception.ErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtTokenErrorCode {
    JWT_INVALID_TOKEN("INFO-500", "Invalid JWT Token"),
    JWT_ACCESS_TOKEN_EXPIRED("INFO-501", "Access Token이 만료되었습니다."),
    JWT_UNSUPPORTED_TOKEN("INFO-502", "Unsupported JWT Token"),
    JWT_CLAIMS_EMPTY("INFO-503", "JWT claims string is empty."),
    JWT_ACCESS_TOKEN_NOT_EXIST("INFO-504", "JWT Access Token이 존재하지 않습니다."),
    JWT_REFRESH_TOKEN_NOT_EXIST("INFO-505", "JWT Refresh Token이 존재하지 않습니다."),
    JWT_REFRESH_TOKEN_NOT_SAME("INFO-506", "JWT Refresh 토큰이 일치하지 않습니다."),
    JWT_ACCESS_TOKEN_MALFORMED_ERROR("INFO-507", "잘못된 JWT Access Token 서명입니다."),
    JWT_REFRESH_TOKEN_MALFORMED_ERROR("INFO-507", "잘못된 JWT Refresh Token 서명입니다.")
    ;

    private final String errorCode;
    private final String errorMessage;

    public static ErrorResponse createErrorResponse(JwtTokenErrorCode errorCode) {
        return ErrorResponse.builder()
                .errorCode(errorCode.getErrorCode())
                .errorMessage(errorCode.getErrorMessage())
                .build();
    }
}
