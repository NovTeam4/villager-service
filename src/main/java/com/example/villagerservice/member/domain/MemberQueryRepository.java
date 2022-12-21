package com.example.villagerservice.member.domain;

import com.example.villagerservice.member.dto.MemberDetail;

public interface MemberQueryRepository {
    MemberDetail.Response getMyPage(Long memberId);
    boolean getFollowState(Long fromMemberId, Long toMemberId);
}
