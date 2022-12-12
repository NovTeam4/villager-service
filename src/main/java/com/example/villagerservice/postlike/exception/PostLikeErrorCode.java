package com.example.villagerservice.postlike.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostLikeErrorCode {
    POST_LIKE_VALID_NOT("INFO-900", "요청인자가 잘못되었습니다."),
    POST_LIKE_NOT_FOUND("INFO-901", "게시글 좋아요가 존재하지않습니다."),
    ;

    private final String errorCode;
    private final String errorMessage;
}
