package com.example.villagerservice.party.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PartyErrorCode {
    //600번대

    PARTY_NOT_FOUND_MEMBER("INFO-600","일치하는 회원이 없습니다"),
    ;

    private final String errorCode;
    private final String errorMessage;
}
