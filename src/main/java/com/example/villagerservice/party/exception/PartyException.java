package com.example.villagerservice.party.exception;

import com.example.villagerservice.common.exception.VillagerException;
import com.example.villagerservice.member.exception.MemberErrorCode;
import lombok.Getter;

@Getter
public class PartyException extends VillagerException {

    private final PartyErrorCode partyErrorCode;

    public PartyException(PartyErrorCode errorCode) {
        super(errorCode.getErrorCode(), errorCode.getErrorMessage());
        this.partyErrorCode = errorCode;
    }
}
