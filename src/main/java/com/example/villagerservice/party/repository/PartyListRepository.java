package com.example.villagerservice.party.repository;

import com.example.villagerservice.party.domain.PartyList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyListRepository extends JpaRepository<PartyList, Long> {
    boolean existsByParty_Member_EmailAndParty_Id(String email, Long partyId);
}
