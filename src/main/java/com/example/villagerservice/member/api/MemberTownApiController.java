package com.example.villagerservice.member.api;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.dto.CreateMemberTown;
import com.example.villagerservice.member.dto.FindMemberTownDetail;
import com.example.villagerservice.member.dto.FindMemberTownList;
import com.example.villagerservice.member.dto.UpdateMemberTown;
import com.example.villagerservice.member.service.MemberTownQueryService;
import com.example.villagerservice.member.service.MemberTownService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members/towns")
public class MemberTownApiController {

    private final MemberTownService memberTownService;
    private final MemberTownQueryService memberTownQueryService;

    @GetMapping
    public FindMemberTownList.Response getMemberTownList(@AuthenticationPrincipal Member member) {
        return memberTownQueryService.getMemberTownList(member.getId());
    }

    @GetMapping("/{memberTownId}")
    public FindMemberTownDetail.Response getMemberTownDetail(@PathVariable Long memberTownId) {
        return memberTownQueryService.getMemberTownDetail(memberTownId);
    }

    @PostMapping
    public void createMemberTown(@AuthenticationPrincipal Member member,
                                 @Valid @RequestBody CreateMemberTown.Request request) {
        memberTownService.addMemberTown(member.getId(), request);
    }

    @PatchMapping("/{member-town-id}")
    public void updateMemberTownName(@AuthenticationPrincipal Member member,
                                     @PathVariable("member-town-id") Long id,
                                     @Valid @RequestBody UpdateMemberTown.Request request) {
        memberTownService.updateMemberTownName(member.getId(), id, request);
    }

    @DeleteMapping("/{member-town-id}")
    public void deleteMemberTown(@AuthenticationPrincipal Member member,
                                 @PathVariable("member-town-id") Long id) {
        memberTownService.deleteMemberTown(member.getId(), id);
    }
}
