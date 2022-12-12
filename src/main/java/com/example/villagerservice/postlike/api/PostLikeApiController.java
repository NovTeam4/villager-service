package com.example.villagerservice.postlike.api;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.postlike.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/postlike")
public class PostLikeApiController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}")
    public void postLikeCheck(@AuthenticationPrincipal Member member,
                              @PathVariable Long postId) {
        postLikeService.postLikeCheck(member.getId(), postId);
    }
}
