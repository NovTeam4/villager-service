package com.example.villagerservice.party.service;

import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.dto.PartyListDTO;
import com.example.villagerservice.party.dto.UpdatePartyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PartyService {

    public void createParty(Long memberId , PartyDTO.Request partyRequest);

    public void deleteParty(Long partyId);

    public PartyDTO.Response updateParty(Long partyId , UpdatePartyDTO.Request updatePartyRequest);
    PartyDTO.Response getParty(Long partyId);
}
