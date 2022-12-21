package com.example.villagerservice.follow.service;

public interface FollowService {
    void following(Long fromMemberId, Long toMemberId);
    void unFollowing(Long fromMemberId, Long toMemberId);
}
