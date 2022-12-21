package com.example.villagerservice.party.service;

import com.example.villagerservice.party.dto.PartyApplyDto;
import com.example.villagerservice.party.dto.PartyApplyDto.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PartyApplyService {

    /**
     * 모임 신청
     * @param targetMemberId
     * @param partyId
     * @return
     */
    PartyApplyDto.Response applyParty(Long targetMemberId, Long partyId);

    /**
     * 모임 신청 리스트 반환
     * @param partyId
     * @param pageable
     * @return
     */
    Page<Response> getApplyPartyList(Long partyId, Pageable pageable);

    /**
     * 모임 허가
     * @param partyId
     * @param targetMemberId
     * @param email
     * @return
     */
    Response partyPermission(Long partyId, Long targetMemberId, String email);
}
