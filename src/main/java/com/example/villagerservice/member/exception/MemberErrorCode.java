package com.example.villagerservice.member.exception;

import com.example.villagerservice.common.exception.ErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode {
    MEMBER_AUTH_NOT_VALID("INFO-400", "ID 또는 비밀번호가 일치하지 않습니다."),
    MEMBER_DUPLICATE_ERROR("INFO-401", "회원이 중복되었습니다."),
    MEMBER_NOT_FOUND( "INFO-402", "회원이 존재하지 않습니다."),
    ;

    private final String errorCode;
    private final String errorMessage;

    public static ErrorResponse createErrorResponse(MemberErrorCode errorCode) {
        return ErrorResponse.builder()
                .errorCode(errorCode.getErrorCode())
                .errorMessage(errorCode.getErrorMessage())
                .build();
    }
}
