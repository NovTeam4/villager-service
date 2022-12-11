package com.example.villagerservice.follow.domain;

import com.example.villagerservice.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFromMemberAndToMember(Member fromMember, Member toMember);

    Optional<Follow> findByFromMemberAndToMember(Member fromMember, Member toMember);


    @Query("select f from Follow f " +
            " join fetch f.fromMember " +
            " join fetch f.toMember " +
            " where f.toMember.id = :toMemberId")
    Optional<Follow> getFollowFetchJoin(@Param("toMemberId") Long toMemberId);
}
