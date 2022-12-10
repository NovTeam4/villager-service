package com.example.villagerservice.party.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PartyApplyErrorCode {
    //700번대
    PARTY_NOT_FOUND("INFO-700","일치하는 모임이 없습니다"),
    ALREADY_BEAN_APPLIED("INFO-701","이미 신청된 모임입니다."),
    DIFFERENT_HOST("INFO-702","사용자가 등록한 모임이 아닙니다."),
    PARTY_APPLY_NOT_FOUND("INFO-703","일치하는 모임 신청이 없습니다.")
    ;

    private final String errorCode;
    private final String errorMessage;

}
