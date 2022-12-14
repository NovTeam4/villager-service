package com.example.villagerservice.member.service;

import com.example.villagerservice.member.dto.MemberDetail;

public interface MemberQueryService {
    MemberDetail.Response getMyPage(Long memberId);
    MemberDetail.Response getOtherMyPage(Long memberId, Long otherMemberId);
}
