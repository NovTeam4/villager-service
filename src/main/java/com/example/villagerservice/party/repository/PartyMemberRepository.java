package com.example.villagerservice.party.repository;

import com.example.villagerservice.party.domain.PartyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {
    boolean existsByParty_Id(Long partyId);
}
