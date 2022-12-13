package com.example.villagerservice.post.service;

import com.example.villagerservice.post.dto.CommentPost;

public interface PostCommentService {
    void createComment(Long memberId, Long postId, CommentPost.Request request);
    void deleteComment(Long memberId, Long postId, Long commentId);

}
