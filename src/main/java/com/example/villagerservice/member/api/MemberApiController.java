package com.example.villagerservice.member.api;


import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.dto.*;
import com.example.villagerservice.member.service.MemberQueryService;
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
    private final MemberQueryService memberQueryService;

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

    @PostMapping("/password/valid")
    public PasswordValid.Response getPasswordValid(
            @AuthenticationPrincipal Member member,
            @RequestBody PasswordValid.Request request) {
        return getPasswordValidResponse(memberService.passwordCheckValid(member.getId(), request));
    }

    @GetMapping
    public MemberDetail.Response getMyPage(@AuthenticationPrincipal Member member) {
        return memberQueryService.getMyPage(member.getId());
    }

    @GetMapping("/{memberId}")
    public MemberDetail.Response getOtherMemberPage(
            @AuthenticationPrincipal Member member,
            @PathVariable Long memberId) {
        return memberQueryService.getOtherMyPage(member.getId(), memberId);
    }

    private PasswordValid.Response getPasswordValidResponse(boolean result) {
        String message = (result) ? "비밀번호가 일치합니다." : "비밀번호가 일치하지 않습니다.";
        return PasswordValid.Response.builder()
                .result(result)
                .message(message)
                .build();
    }
}
