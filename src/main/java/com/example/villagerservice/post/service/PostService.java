package com.example.villagerservice.post.service;

import com.example.villagerservice.post.dto.CreatePost;
import org.springframework.stereotype.Service;


public interface PostService {
    void createPost(Long memberId, CreatePost.Request request);


}
