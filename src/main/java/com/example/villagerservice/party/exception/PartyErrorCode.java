package com.example.villagerservice.party.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PartyErrorCode {

    PARTY_NOT_FOUND_MEMBER("일치하는 회원이 없습니다");

    private final String errorMessage;


}
