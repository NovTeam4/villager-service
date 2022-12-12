package com.example.villagerservice.party.exception;

import com.example.villagerservice.common.exception.VillagerException;
import lombok.Getter;

@Getter
public class PartyCommentException extends VillagerException {

    private final PartyCommentErrorCode partyCommentErrorCode;

    public PartyCommentException(PartyCommentErrorCode partyCommentErrorCode) {
        super(partyCommentErrorCode.getErrorCode(), partyCommentErrorCode.getErrorMessage());
        this.partyCommentErrorCode = partyCommentErrorCode;
    }
}
