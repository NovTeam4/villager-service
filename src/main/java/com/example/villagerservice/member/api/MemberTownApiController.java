package com.example.villagerservice.member.api;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.dto.CreateMemberTown;
import com.example.villagerservice.member.service.MemberTownService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members/towns")
public class MemberTownApiController {

    private final MemberTownService memberTownService;

    @PostMapping
    public void createMemberTown(@AuthenticationPrincipal Member member,
                                 @Valid @RequestBody CreateMemberTown.Request request) {
        memberTownService.addMemberTown(member.getId(), request);
    }
}
