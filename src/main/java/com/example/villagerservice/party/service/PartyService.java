package com.example.villagerservice.party.service;

import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.request.PartyCreate;

public interface PartyService {

    public void createParty(Long memberId , PartyDTO.Request partyRequest);

}
