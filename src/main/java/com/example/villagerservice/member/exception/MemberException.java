package com.example.villagerservice.member.exception;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {
    private final MemberErrorCode errorCode;

    public MemberException(MemberErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
