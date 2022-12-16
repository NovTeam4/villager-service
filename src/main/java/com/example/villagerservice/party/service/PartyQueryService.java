package com.example.villagerservice.party.service;

import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.dto.PartyListDTO;

import java.util.List;

public interface PartyQueryService {

    public List<PartyListDTO> getPartyList(Double lat , Double lnt);

}
