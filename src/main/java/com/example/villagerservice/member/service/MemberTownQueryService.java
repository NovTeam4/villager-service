package com.example.villagerservice.member.service;

import com.example.villagerservice.member.dto.FindMemberTownDetail;
import com.example.villagerservice.member.dto.FindMemberTownList;

public interface MemberTownQueryService {

    FindMemberTownList.Response getMemberTownList(Long memberId);
    FindMemberTownDetail.Response getMemberTownDetail(Long memberTownId);

}
