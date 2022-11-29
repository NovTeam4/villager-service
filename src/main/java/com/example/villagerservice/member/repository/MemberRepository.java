package com.example.villagerservice.member.repository;

import com.example.villagerservice.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
