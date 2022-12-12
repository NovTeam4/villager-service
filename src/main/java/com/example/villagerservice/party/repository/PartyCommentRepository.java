package com.example.villagerservice.party.repository;

import com.example.villagerservice.party.domain.PartyComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyCommentRepository extends JpaRepository<PartyComment , Long> {

    public PartyComment findByParty_id(Long partyId);
}
