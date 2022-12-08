package com.example.villagerservice.member.api;


import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.dto.CreateMemberAttentionTag;
import com.example.villagerservice.member.dto.UpdateMemberInfo;
import com.example.villagerservice.member.dto.UpdateMemberPassword;
import com.example.villagerservice.member.dto.ValidMemberNickname;
import com.example.villagerservice.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/valid/nickname")
    public void validNickname(@Valid @RequestBody ValidMemberNickname.Request request) {
        memberService.validNickname(request);
    }

    @PatchMapping("/info")
    public void updateMemberInfo(@AuthenticationPrincipal Member member,
                                 @Valid @RequestBody UpdateMemberInfo.Request updateMemberInfo) {
        memberService.updateMemberInfo(member.getEmail(), updateMemberInfo);
    }

    @PatchMapping("/password")
    public void updateMemberPassword(@AuthenticationPrincipal Member member,
                                     @Valid @RequestBody UpdateMemberPassword.Request updateMemberPassword) {
        memberService.updateMemberPassword(member.getEmail(), updateMemberPassword);
    }

    @DeleteMapping
    public void deleteMember(@AuthenticationPrincipal Member member) {
        memberService.deleteMember(member.getEmail());
    }

    @PostMapping("/tags")
    public void addMemberAttentionTag(@AuthenticationPrincipal Member member,
                                      @Valid @RequestBody CreateMemberAttentionTag.Request addAttentionTag) {
        memberService.addAttentionTag(member.getEmail(), addAttentionTag);
    }
}
