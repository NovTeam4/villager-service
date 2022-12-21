package com.example.villagerservice.party.service;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.party.request.PartyLikeDto;

public interface PartyLikeService {

    /**
     * 관심모임 등록/취소 기능
     * @param partyId
     * @param member
     * @return
     */
    PartyLikeDto.Response partyLike(Long partyId, Member member);

    /**
     * 관심모임 여부 확인
     * @param partyId
     * @param email
     * @return
     */
    boolean isPartyLike(Long partyId, String email);
}
