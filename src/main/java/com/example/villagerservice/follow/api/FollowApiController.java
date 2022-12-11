package com.example.villagerservice.follow.api;

import com.example.villagerservice.follow.service.FollowService;
import com.example.villagerservice.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FollowApiController {
    private final FollowService followService;
    @PostMapping("/follow/{followId}")
    public void following(@AuthenticationPrincipal Member member,
                          @PathVariable Long followId) {
        followService.following(member.getId(), followId);
    }

    @PostMapping("/unfollow/{followId}")
    public void unFollowing(@AuthenticationPrincipal Member member,
                          @PathVariable Long followId) {
        followService.unFollowing(member.getId(), followId);
    }
}
