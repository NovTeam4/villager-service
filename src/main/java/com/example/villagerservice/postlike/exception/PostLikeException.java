package com.example.villagerservice.postlike.exception;

import com.example.villagerservice.common.exception.VillagerException;
import lombok.Getter;

@Getter
public class PostLikeException extends VillagerException {
    private final PostLikeErrorCode postLikeErrorCode;
    public PostLikeException(PostLikeErrorCode errorCode) {
        super(errorCode.getErrorCode(), errorCode.getErrorMessage());
        this.postLikeErrorCode = errorCode;
    }
}