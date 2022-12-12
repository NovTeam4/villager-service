package com.example.villagerservice.post.service;

import com.example.villagerservice.post.dto.CreatePost;
import com.example.villagerservice.post.dto.UpdatePost;


public interface PostService {
    void createPost(Long memberId, CreatePost.Request request);  // 게시글 작성
    void updatePost(Long memberId, Long postId,UpdatePost.Request request);  // 게시글 수정

    void deletePost(Long memberId, Long postId);
}
