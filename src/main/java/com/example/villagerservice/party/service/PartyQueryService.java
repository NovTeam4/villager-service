package com.example.villagerservice.party.service;

import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.dto.PartyDTO;

public interface PartyQueryService {

    PartyDTO.Response getParty(Long partyId);

}
