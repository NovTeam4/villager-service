package com.example.villagerservice.post.exception;

import com.example.villagerservice.common.exception.VillagerException;
import com.example.villagerservice.member.exception.MemberErrorCode;
import lombok.Getter;

@Getter
public class PostException extends VillagerException {
    private final PostErrorCode postErrorCode;
    public PostException(PostErrorCode errorCode) {
        super(errorCode.getErrorCode(), errorCode.getErrorMessage());
        this.postErrorCode = errorCode;
    }
}