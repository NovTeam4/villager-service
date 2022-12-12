package com.example.villagerservice.postlike.service;

public interface PostLikeService {
    void postLikeCheck(Long memberId, Long postId);
    void postLikeUnCheck(Long memberId, Long postId);
}
