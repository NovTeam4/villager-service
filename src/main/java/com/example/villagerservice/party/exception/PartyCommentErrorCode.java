package com.example.villagerservice.party.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PartyCommentErrorCode {

    CONTENT_IS_REQUIRED("INFO-620","내용은 필수 입니다.");

    private final String errorCode;
    private final String errorMessage;
}
