package com.example.villagerservice.party.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PartyListErrorCode {
    //700번대
    PARTY_NOT_FOUND("INFO-700","일치하는 모임이 없습니다"),
    ALREADY_BEAN_APPLIED("INFO-701","이미 신청된 모임입니다.")

    ;

    private final String errorCode;
    private final String errorMessage;


}
