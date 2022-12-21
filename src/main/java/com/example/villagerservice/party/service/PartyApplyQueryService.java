package com.example.villagerservice.party.service;

import com.example.villagerservice.party.domain.PartyApply;
import java.util.List;

public interface PartyApplyQueryService {

    List<PartyApply> getPartyApplyId(Long partyId, String email);
}
