package com.example.villagerservice.post.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode {
    POST_VALID_NOT("INFO-800", "요청인자가 잘못되었습니다."),
    POST_NOT_FOUND("INFO-804", "게시글의 ID가 존재하지않습니다."),
    POST_DELETE_NOT_FOUND("INFO-814", "이미 삭제 대기중인 게시글입니다."),
    ;

    private final String errorCode;
    private final String errorMessage;
}
