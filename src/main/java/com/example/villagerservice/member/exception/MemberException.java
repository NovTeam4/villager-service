package com.example.villagerservice.member.exception;

import com.example.villagerservice.common.exception.VillagerException;
import lombok.Getter;

@Getter
public class MemberException extends VillagerException {
    private final MemberErrorCode memberErrorCode;

    public MemberException(MemberErrorCode errorCode) {
        super(errorCode.getErrorCode(), errorCode.getErrorMessage());
        this.memberErrorCode = errorCode;
    }
}