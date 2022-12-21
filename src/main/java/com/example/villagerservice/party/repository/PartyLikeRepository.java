package com.example.villagerservice.party.repository;

import com.example.villagerservice.party.domain.PartyLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartyLikeRepository extends JpaRepository<PartyLike, Long> {
    Optional<PartyLike> findByParty_IdAndMember_Email(Long partyId, String email);

    boolean existsByParty_IdAndMember_Email(Long partyId, String email);
}
