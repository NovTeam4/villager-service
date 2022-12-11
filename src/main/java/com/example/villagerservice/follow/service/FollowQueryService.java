package com.example.villagerservice.follow.service;

import com.example.villagerservice.follow.dto.FollowList;

public interface FollowQueryService {
    FollowList.Response getFollowList(FollowList.Request request);
}
