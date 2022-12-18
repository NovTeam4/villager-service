package com.example.villagerservice.party.repository;

import com.example.villagerservice.party.domain.PartyLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartyLikeRepository extends JpaRepository<PartyLike, Long> {
    Optional<PartyLike> findByParty_IdAndMember_Email(Long partyId, String email);

    @Query("select pl from PartyLike pl join fetch pl.party join fetch pl.member " +
            " where pl.party.id = :partyId and " +
            " pl.member.email = :email")
    boolean existByPartyIdAndMemberEmail(@Param("partyId") Long partyId, @Param("email") String email);
}
