package com.example.villagerservice.member.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @Query("select m from Member m join fetch m.memberDetail where m.email = :email")
    Optional<Member> findByMemberDetail(@Param("email") String email);

    @Query("select m from Member m " +
            " join fetch m.tagCollection.tags " +
            " join fetch m.memberDetail " +
            " where m.id = :memberId")
    Optional<Member> findByMemberWithTag(@Param("memberId") Long memberId);
}
