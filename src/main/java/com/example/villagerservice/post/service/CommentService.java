package com.example.villagerservice.post.service;

import com.example.villagerservice.post.dto.CommentPost;

public interface CommentService {
    void createComment(Long memberId, Long postId, CommentPost.Request request);
}
