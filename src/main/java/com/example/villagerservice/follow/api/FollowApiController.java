package com.example.villagerservice.follow.api;

import com.example.villagerservice.follow.dto.FollowList;
import com.example.villagerservice.follow.service.FollowQueryService;
import com.example.villagerservice.follow.service.FollowService;
import com.example.villagerservice.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FollowApiController {
    private final FollowService followService;
    private final FollowQueryService followQueryService;
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

    @GetMapping("/follows")
    public FollowList.Response getFollowList(@ModelAttribute FollowList.Request request) {
        return followQueryService.getFollowList(request);
    }
}
