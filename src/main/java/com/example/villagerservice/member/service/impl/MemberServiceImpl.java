package com.example.villagerservice.member.service.impl;

import com.example.villagerservice.member.exception.MemberException;
import com.example.villagerservice.member.repository.MemberRepository;
import com.example.villagerservice.member.request.MemberCreate;
import com.example.villagerservice.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_DUPLICATE_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void createMember(MemberCreate memberCreate) {
        // 이메일 중복 체크
        memberCheckedEmailValid(memberCreate.getEmail());
        memberRepository.save(memberCreate.toEntity());
    }

    private void memberCheckedEmailValid(String email) {
        memberRepository.findByEmail(email)
                .ifPresent(m -> {
                    throw new MemberException(MEMBER_DUPLICATE_ERROR);
                });
    }
}
