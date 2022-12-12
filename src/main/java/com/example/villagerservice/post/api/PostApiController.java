package com.example.villagerservice.post.api;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.post.dto.CreatePost;
import com.example.villagerservice.post.dto.UpdatePost;
import com.example.villagerservice.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostApiController {
    private final PostService postService;

    @PostMapping
    public void createPost(@AuthenticationPrincipal Member member,
                           @Valid @RequestBody CreatePost.Request request) {
        postService.createPost(member.getId(), request);
    }

    @PutMapping("/{id}")
    public void updatePost(@AuthenticationPrincipal Member member,
                           @PathVariable("id") Long postId,
                           @Valid @RequestBody UpdatePost.Request request) {
        postService.updatePost(member.getId(), postId, request);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@AuthenticationPrincipal Member member,
                           @PathVariable("id") Long postId
                           ) {
        postService.deletePost(member.getId(), postId);
    }
}
