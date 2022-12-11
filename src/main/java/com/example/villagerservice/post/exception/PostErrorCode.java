package com.example.villagerservice.post.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode {
    MEMBER_VALID_NOT("INFO-800", "요청인자가 잘못되었습니다."),
    ;

    private final String errorCode;
    private final String errorMessage;
}
