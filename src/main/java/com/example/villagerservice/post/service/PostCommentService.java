package com.example.villagerservice.post.service;

import com.example.villagerservice.post.dto.CreatePostComment;

public interface PostCommentService {
    void createPostComment(Long memberId, Long postId, CreatePostComment.Request request);
    void deleteComment(Long memberId, Long postId, Long commentId);

}
