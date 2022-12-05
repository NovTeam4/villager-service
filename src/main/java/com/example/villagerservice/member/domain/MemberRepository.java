package com.example.villagerservice.member.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @Query("select m from Member m join fetch m.memberDetail where m.email = :email")
    Optional<Member> findByMemberDetail(@Param("email") String email);
}
