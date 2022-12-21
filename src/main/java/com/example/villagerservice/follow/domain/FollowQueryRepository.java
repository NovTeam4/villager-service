package com.example.villagerservice.follow.domain;

import com.example.villagerservice.follow.dto.FollowList;

public interface FollowQueryRepository {
    FollowList.Response getFollowList(FollowList.Request request);
}
