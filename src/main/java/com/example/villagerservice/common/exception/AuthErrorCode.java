package com.example.villagerservice.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode {
    AUTH_INFO_NOT_VALID("INFO-001", "ID 또는 비밀번호가 일치하지 않습니다."),
    AUTH_MEMBER_ALREADY_DELETED("INFO-002", "이미 탈퇴된 회원입니다.")
    ;

    private final String errorCode;
    private final String errorMessage;
}
