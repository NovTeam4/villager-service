package com.example.villagerservice.member.service.impl;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberDetailRepository;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.dto.*;
import com.example.villagerservice.member.exception.MemberException;
import com.example.villagerservice.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.villagerservice.member.exception.MemberErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberDetailRepository memberDetailRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createMember(CreateMember.Request createMember) {
        // 이메일 중복 체크
        memberCheckedEmailValid(createMember.getEmail());
        // 닉네임 중복 체크
        memberCheckedNicknameDuplicateValid(createMember.getNickname());
        // 비밀번호 암호화
        createMember.passwordEncrypt(passwordEncoder);
        memberRepository.save(createMember.toEntity());
    }

    @Override
    @Transactional
    public void updateMemberInfo(String email, UpdateMemberInfo.Request updateMemberInfo) {
        Member member = findByMemberEmail(email);
        member.updateMemberInfo(updateMemberInfo.getNickname(), updateMemberInfo.getIntroduce());
    }

    @Override
    @Transactional
    public void updateMemberPassword(String email, UpdateMemberPassword.Request passwordUpdate) {
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
    public void addAttentionTag(String email, CreateMemberAttentionTag.Request addAttentionTag) {
        Member member = findByMemberEmail(email);
        member.addMemberAttentionTag(addAttentionTag.toEntity());
    }


    @Override
    @Transactional(readOnly = true)
    public void validNickname(ValidMemberNickname.Request request) {
        memberCheckedNicknameDuplicateValid(request.getNickname());
    }

    private void memberCheckedNicknameDuplicateValid(String nickname) {
        boolean result = memberDetailRepository.existsByNickname(nickname);

        if (result) {
            throw new MemberException(MEMBER_NICKNAME_DUPLICATE_ERROR);
        }
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
