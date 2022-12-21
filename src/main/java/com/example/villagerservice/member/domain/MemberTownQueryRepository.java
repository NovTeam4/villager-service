package com.example.villagerservice.member.domain;

import com.example.villagerservice.member.dto.FindMemberTownDetail;
import com.example.villagerservice.member.dto.FindMemberTownList;

public interface MemberTownQueryRepository {
    FindMemberTownList.Response getMemberTownList(Long memberTownId);
    FindMemberTownDetail.Response getMemberTownDetail(Long memberTownId);
}
