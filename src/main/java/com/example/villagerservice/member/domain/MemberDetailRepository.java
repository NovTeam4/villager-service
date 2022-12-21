package com.example.villagerservice.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberDetailRepository extends JpaRepository<MemberDetail, Long> {
    boolean existsByNickname(String nickname);
}
