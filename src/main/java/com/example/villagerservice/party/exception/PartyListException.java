package com.example.villagerservice.party.exception;

import com.example.villagerservice.common.exception.VillagerException;
import lombok.Getter;

@Getter
public class PartyListException extends VillagerException {

    private final PartyListErrorCode partyListErrorCode;

    public PartyListException(PartyListErrorCode errorCode) {
        super(errorCode.getErrorCode(), errorCode.getErrorMessage());
        this.partyListErrorCode = errorCode;
    }
}
