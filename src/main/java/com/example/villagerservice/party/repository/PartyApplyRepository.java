package com.example.villagerservice.party.repository;

import com.example.villagerservice.party.domain.PartyApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyApplyRepository extends JpaRepository<PartyApply, Long> {
    boolean existsByParty_Member_EmailAndParty_Id(String email, Long partyId);

    Page<PartyApply> findByParty_Id(Long partyId, Pageable pageable);
}
