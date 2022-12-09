package com.example.villagerservice.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode {
    MEMBER_VALID_NOT("INFO-400", "요청인자가 잘못되었습니다."),
    MEMBER_DUPLICATE_ERROR("INFO-402", "회원이 중복되었습니다."),
    MEMBER_NOT_FOUND("INFO-403", "회원이 존재하지 않습니다."),
    MEMBER_UPDATE_SAME_PASS("INFO-404", "비밀번호가 동일합니다."),
    MEMBER_TAG_MAX_COUNT("INFO-405", "관심태그는 최대 5개까지 가능합니다."),
    MEMBER_NICKNAME_DUPLICATE_ERROR("INFO-406", "이미 존재하는 닉네임입니다. 다른 닉네임을 입력해주세요."),
    MEMBER_TOWN_ADD_MAX("INFO-407", "회원 동네는 최대 2개까지 가능합니다."),
    ;

    private final String errorCode;
    private final String errorMessage;
}
