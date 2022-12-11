package com.example.villagerservice.follow.exception;

import com.example.villagerservice.common.exception.VillagerException;
import lombok.Getter;

@Getter
public class FollowException extends VillagerException {
    private final FollowErrorCode memberErrorCode;

    public FollowException(FollowErrorCode errorCode) {
        super(errorCode.getErrorCode(), errorCode.getErrorMessage());
        this.memberErrorCode = errorCode;
    }
}