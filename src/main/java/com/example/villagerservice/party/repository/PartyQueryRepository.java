package com.example.villagerservice.party.repository;

import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.dto.PartyDTO;

import java.util.Optional;

public interface PartyQueryRepository {
    Optional<PartyDTO.Response> getParty(Long partyId);

    Optional<Party> findById(Long partyId);

}
