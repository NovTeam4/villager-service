package com.example.villagerservice.follow.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FollowErrorCode {
    FOLLOW_INTERNAL_ERROR("INFO-800", "팔로우 내부 에러"),
    FOLLOW_ALREADY_STATUS("INFO-801", "이미 팔로우된 상태입니다."),
    UNFOLLOW_ALREADY_STATUS("INFO-802", "이미 언팔로우된 상태입니다."),
    FOLLOW_APPLICATION_NOT_FOUND("INFO-803", "팔로우 신청자가 존재하지 않습니다."),
    ;

    private final String errorCode;
    private final String errorMessage;
}
