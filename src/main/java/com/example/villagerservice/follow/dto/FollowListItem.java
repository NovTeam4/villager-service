package com.example.villagerservice.follow.dto;

import lombok.Getter;

@Getter
public class FollowListItem {
    private String nickName;
    private Long followCount;

    public FollowListItem(String nickName, Long followCount) {
        this.nickName = nickName;
        this.followCount = followCount;
    }
}
