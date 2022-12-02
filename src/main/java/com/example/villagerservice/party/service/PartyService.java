package com.example.villagerservice.party.service;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.party.request.PartyCreate;

public interface PartyService {

    public void createParty(String email , PartyCreate partyCreate);

}
