package com.example.villagerservice.comment.excepiton;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode {
    COMMENT_PAGE_NOT_FOUND("INFO-914", "요청한 인자값 Page 범위를 확인해주세요."),
    ;

    private final String errorCode;
    private final String errorMessage;
}
