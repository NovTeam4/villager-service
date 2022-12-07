package com.example.villagerservice.party.service;

import com.example.villagerservice.party.request.PartyApplyDto;
import com.example.villagerservice.party.request.PartyApplyDto.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PartyApplyService {

    /**
     * 모임 신청
     * @param email
     * @param partyId
     * @return
     */
    PartyApplyDto.Response applyParty(String email, Long partyId);

    /**
     * 모임 신청 리스트 반환
     * @param partyId
     * @param pageable
     * @return
     */
    Page<Response> getApplyPartyList(Long partyId, Pageable pageable);
}
