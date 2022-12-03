package com.example.villagerservice.member.service.impl;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.exception.MemberException;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.request.MemberAddAttentionTag;
import com.example.villagerservice.member.request.MemberCreate;
import com.example.villagerservice.member.request.MemberInfoUpdate;
import com.example.villagerservice.member.request.MemberPasswordUpdate;
import com.example.villagerservice.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_DUPLICATE_ERROR;
import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createMember(MemberCreate memberCreate) {
        // 이메일 중복 체크
        memberCheckedEmailValid(memberCreate.getEmail());
        // 비밀번호 암호화
        memberCreate.passwordEncrypt(passwordEncoder);
        memberRepository.save(memberCreate.toEntity());
    }

    @Override
    @Transactional
    public void updateMemberInfo(String email, MemberInfoUpdate memberInfoUpdate) {
        Member member = findByMemberEmail(email);
        member.updateMemberInfo(memberInfoUpdate.getNickname());
    }

    @Override
    @Transactional
    public void updateMemberPassword(String email, MemberPasswordUpdate passwordUpdate) {
        Member member = findByMemberEmail(email);
        member.changePassword(passwordEncoder, passwordUpdate.getPassword());
    }

    @Override
    @Transactional
    public void deleteMember(String email) {
        Member member = findByMemberEmail(email);
        member.deleteMember();
    }

    @Override
    @Transactional
    public void addAttentionTag(String email, MemberAddAttentionTag addAttentionTag) {
        Member member = findByMemberEmail(email);
        member.addMemberAttentionTag(addAttentionTag.toEntity());
    }

    private Member findByMemberEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    private void memberCheckedEmailValid(String email) {
        memberRepository.findByEmail(email)
                .ifPresent(m -> {
                    throw new MemberException(MEMBER_DUPLICATE_ERROR);
                });
    }
}
