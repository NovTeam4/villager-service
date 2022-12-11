package com.example.villagerservice.post.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryErrorCode {
    CATEGORY_NOT_FOUND("INFO-900", "카테고리가 존재하지 않습니다."),
    ;

    private final String errorCode;
    private final String errorMessage;
}
