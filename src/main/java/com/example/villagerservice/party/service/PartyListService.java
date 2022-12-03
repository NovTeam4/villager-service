package com.example.villagerservice.party.service;

public interface PartyListService {

    /**
     * 모임 신청
     * @param email
     * @param partyId
     * @return
     */
    void applyParty(String email, Long partyId);
}
