package com.example.villagerservice.party.repository;

import com.example.villagerservice.party.domain.PartyTag;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PartyTagRepository extends JpaRepository<PartyTag , Long> {
    void deleteAllByParty_id(Long partyId);

}
