package com.example.villagerservice.post.service;

import com.example.villagerservice.post.dto.CreatePostComment;
import com.example.villagerservice.post.dto.UpdatePostComment;

public interface PostCommentService {
    void createPostComment(Long memberId, Long postId, CreatePostComment.Request request);
    void deletePostComment(Long memberId, Long postId, Long commentId);
    void updatePostComment(Long memberId, Long postId, Long commentId, UpdatePostComment.Request request);

}
