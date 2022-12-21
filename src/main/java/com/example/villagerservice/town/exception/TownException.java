package com.example.villagerservice.town.exception;

import com.example.villagerservice.common.exception.VillagerException;
import com.example.villagerservice.member.exception.MemberErrorCode;
import lombok.Getter;

@Getter
public class TownException extends VillagerException {
    private final TownErrorCode townErrorCode;

    public TownException(TownErrorCode errorCode) {
        super(errorCode.getErrorCode(), errorCode.getErrorMessage());
        this.townErrorCode = errorCode;
    }
}