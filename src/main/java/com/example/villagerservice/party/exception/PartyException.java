package com.example.villagerservice.party.exception;

import com.example.villagerservice.member.exception.MemberErrorCode;
import lombok.Getter;

@Getter
public class PartyException extends RuntimeException{

    private final PartyErrorCode errorCode;

    public PartyException(PartyErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
