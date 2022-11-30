package com.example.villagerservice.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode {
    MEMBER_VALIDATE_ERROR("유효성 검사에 실패하였습니다."),
    MEMBER_DUPLICATE_ERROR("회원이 중복되었습니다."),
    MEMBER_NOT_FOUND("회원이 존재하지 않습니다."),
    ;

    private final String errorMessage;
}
