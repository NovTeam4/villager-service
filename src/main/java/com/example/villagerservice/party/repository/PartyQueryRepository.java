package com.example.villagerservice.party.repository;

import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.dto.PartyListDTO;

import java.util.List;

public interface PartyQueryRepository {

    public List<PartyListDTO> getPartyList(String email , Double lat , Double lnt);
}
