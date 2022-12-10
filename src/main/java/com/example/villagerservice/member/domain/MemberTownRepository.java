package com.example.villagerservice.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberTownRepository extends JpaRepository<MemberTown, Long> {

    Long countByMember(Member member);
    boolean existsByMemberAndTownName(Member member, String townName);

    @Query("select mt from MemberTown mt " +
            " join fetch mt.member " +
            " join fetch mt.town " +
            " where mt.town.id = :townId")
    List<MemberTown> getMemberTownFetchJoinByTownId(@Param("townId") Long townId);

    @Query("select mt from MemberTown  mt" +
            " join fetch mt.member " +
            " where mt.id = :memberTownId")
    Optional<MemberTown> getMemberTownWithMember(@Param("memberTownId") Long memberTownId);
}
