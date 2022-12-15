package com.example.villagerservice.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MailErrorCode {
    FAILED_SEND_MASSAGE("INFO-900", "이메일 전송에 실패했습니다.")
    ;

    private final String errorCode;
    private final String errorMessage;
}
