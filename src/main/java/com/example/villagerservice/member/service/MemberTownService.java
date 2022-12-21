package com.example.villagerservice.member.service;

import com.example.villagerservice.member.dto.CreateMemberTown;
import com.example.villagerservice.member.dto.UpdateMemberTown;

public interface MemberTownService {
    void addMemberTown(Long memberId, CreateMemberTown.Request request);
    void updateMemberTown(Long memberId, Long memberTownId, UpdateMemberTown.Request request);
    void deleteMemberTown(Long memberId, Long memberTownId);
}
