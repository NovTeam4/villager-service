package com.example.villagerservice.post.api;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.post.dto.*;
import com.example.villagerservice.post.service.PostCommentService;
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
    private final PostCommentService postCommentService;
    private final PostQueryService postQueryService;

    @GetMapping
    public Page<ListPostItem> getPostList(@ModelAttribute ListPostSearchCond searchCond,
                                          @PageableDefault Pageable pageable) {
        return postQueryService.getPostList(searchCond, pageable);
    }

    @GetMapping("/{postId}")
    public PostItemDetail getPost(@PathVariable Long postId) {
        postService.postViewCountUp(postId);
        return postQueryService.getPostItemDetail(postId);
    }

    @PostMapping
    public void createPost(@AuthenticationPrincipal Member member,
                           @RequestPart(value = "post") CreatePost.Request request,
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
                           @PathVariable("id") Long postId) {
        postService.deletePost(member.getId(), postId);
    }

    @PostMapping("/comments/{id}")
    public void createPostComment(@AuthenticationPrincipal Member member,
                                  @PathVariable("id") Long postId,
                                  @Valid @RequestBody CreatePostComment.Request request){
        postCommentService.createPostComment(member.getId(),postId,request);
    }

    @DeleteMapping("/comments/{postId}/{commentId}")
    public void deleteComment(@AuthenticationPrincipal Member member,
                              @PathVariable("postId") Long postId,
                              @PathVariable("commentId") Long commentId){
        postCommentService.deleteComment(member.getId(),postId,commentId);
    }

}
