package com.example.villagerservice.member.api;


import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.request.MemberInfoUpdate;
import com.example.villagerservice.member.request.MemberPasswordUpdate;
import com.example.villagerservice.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberApiController {

    private final MemberService memberService;

    @PatchMapping("/info")
    public void updateMemberInfo(@AuthenticationPrincipal Member member,
                                 @Valid @RequestBody MemberInfoUpdate memberInfoUpdate) {
        memberService.updateMemberInfo(member.getEmail(), memberInfoUpdate);
    }

    @PatchMapping("/password")
    public void updateMemberPassword(@AuthenticationPrincipal Member member,
                                     @Valid @RequestBody MemberPasswordUpdate memberPasswordUpdate) {
        memberService.updateMemberPassword(member.getEmail(), memberPasswordUpdate.getPassword());
    }
}
