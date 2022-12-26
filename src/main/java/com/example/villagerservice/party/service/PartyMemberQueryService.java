package com.example.villagerservice.party.service;

import java.util.List;

public interface PartyMemberQueryService {
    List<Long> getPartyMemberId(Long partyId, String email);
}
