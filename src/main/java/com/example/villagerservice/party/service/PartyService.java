package com.example.villagerservice.party.service;

import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.dto.UpdatePartyDTO;

public interface PartyService {

    public void createParty(Long memberId , PartyDTO.Request partyRequest);

    public void deleteParty(Long partyId);

    public PartyDTO.Response updateParty(Long partyId , UpdatePartyDTO.Request updatePartyRequest);

}
