package com.example.villagerservice.post.api;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.post.dto.*;
import com.example.villagerservice.post.service.PostQueryService;
import com.example.villagerservice.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostApiController {
    private final PostService postService;
    private final PostQueryService postQueryService;

    @GetMapping
    public Page<ListPostItem> getPostList(@ModelAttribute ListPostSearchCond searchCond,
                                          @PageableDefault Pageable pageable) {
        return postQueryService.getPostList(searchCond, pageable);
    }

    @PostMapping
    public void createPost(@AuthenticationPrincipal Member member,
                           @RequestPart(value = "request") CreatePost.Request request,
                           @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        postService.createPost(member.getId(), request, files);
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
