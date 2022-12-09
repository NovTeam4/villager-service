package com.example.villagerservice.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberTownRepository extends JpaRepository<MemberTown, Long> {

    Long countByMember(Member member);

    @Query("select mt from MemberTown mt " +
            " join fetch mt.member " +
            " join fetch mt.town " +
            " where mt.town.id = :townId")
    List<MemberTown> getMemberTownFetchJoinByTownId(@Param("townId") Long townId);
}
