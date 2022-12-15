package com.example.villagerservice.common.exception;

import com.example.villagerservice.party.exception.PartyApplyErrorCode;
import lombok.Getter;

@Getter
public class MailException extends VillagerException {

    private final MailErrorCode mailErrorCode;

    public MailException(MailErrorCode mailErrorCode) {
        super(mailErrorCode.getErrorCode(), mailErrorCode.getErrorMessage());
        this.mailErrorCode = mailErrorCode;
    }
}
