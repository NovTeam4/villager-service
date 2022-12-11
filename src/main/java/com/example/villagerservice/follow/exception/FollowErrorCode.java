package com.example.villagerservice.follow.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FollowErrorCode {
    FOLLOW_ALREADY_STATUS("INFO-801", "이미 팔로우된 상태입니다."),
    FOLLOW_APPLICATION_NOT_FOUND("INFO-802", "팔로우 신청자가 존재하지 않습니다."),
    ;

    private final String errorCode;
    private final String errorMessage;
}
