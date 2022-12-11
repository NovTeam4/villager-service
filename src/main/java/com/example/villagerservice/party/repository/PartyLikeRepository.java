package com.example.villagerservice.party.repository;

import com.example.villagerservice.party.domain.PartyLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyLikeRepository extends JpaRepository<PartyLike, Long> {
    Optional<PartyLike> findByParty_IdAndMember_Email(Long partyId, String email);
}
