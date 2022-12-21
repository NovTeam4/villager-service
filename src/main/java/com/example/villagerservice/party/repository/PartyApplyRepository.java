package com.example.villagerservice.party.repository;

import com.example.villagerservice.party.domain.PartyApply;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyApplyRepository extends JpaRepository<PartyApply, Long> {
    boolean existsByParty_Member_IdAndParty_Id(Long Id, Long partyId);

    Page<PartyApply> findByParty_Id(Long partyId, Pageable pageable);

    Optional<PartyApply> findByParty_IdAndTargetMemberId(Long partyId, Long targetMemberId);

    Optional<PartyApply> findFirstByOrderByIdDesc();
}
