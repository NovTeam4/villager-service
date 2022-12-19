package com.example.villagerservice.config.security.oauth2.service;

import com.example.villagerservice.config.security.oauth2.enums.SocialType;
import com.example.villagerservice.config.security.oauth2.model.ProviderUser;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void register(String registrationId, ProviderUser providerUser) {
        Member member = Member.builder()
                .email(providerUser.getEmail())
                .nickname(getNickname(providerUser.getEmail()))
                .encodedPassword(providerUser.getPassword())
                .socialType(SocialType.valueOf(registrationId.toUpperCase()))
                .build();

         memberRepository.save(member);
    }

    private String getNickname(String email) {
        int idx = email.indexOf("@");
        return email.substring(0, idx);
    }
}
