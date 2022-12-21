package com.example.villagerservice.party.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PartyErrorCode {
    //600번대

    PARTY_NOT_FOUND_MEMBER("INFO-600","일치하는 회원이 없습니다"),
    PARTY_NOT_FOUND("INFO-601","일치하는 모임이 없습니다"),
    PARTY_NOT_REGISTERED("INFO-602" , "모임이 등록되어 있지 않습니다."),
    PARTY_CHAT_ROOM_NOT_FOUND("INFO-603" , "모임 채팅방이 없습니다."),
    PARTY_IS_NOT_TIME("INFO-604" , "모임 시작시간이 아닙니다."),
    PARTY_MEMBER_EMPTY("INFO-605" , "모임원이 없습니다.")
    ;

    private final String errorCode;
    private final String errorMessage;
}
