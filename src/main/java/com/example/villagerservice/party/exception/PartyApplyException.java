package com.example.villagerservice.party.exception;

import com.example.villagerservice.common.exception.VillagerException;
import lombok.Getter;

@Getter
public class PartyApplyException extends VillagerException {

    private final PartyApplyErrorCode partyApplyErrorCode;

    public PartyApplyException(PartyApplyErrorCode errorCode) {
        super(errorCode.getErrorCode(), errorCode.getErrorMessage());
        this.partyApplyErrorCode = errorCode;
    }
}
