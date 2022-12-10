package com.example.villagerservice.member.domain;

import com.example.villagerservice.member.dto.FindMemberTownList;

public interface MemberTownQueryRepository {
    FindMemberTownList.Response getMemberTownList(Long memberTownId);
}
