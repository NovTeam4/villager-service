package com.example.villagerservice.config.security.oauth2.service;

import com.example.villagerservice.config.security.oauth2.enums.SocialType;
import com.example.villagerservice.config.security.oauth2.model.ProviderUser;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

// @Service
@RequiredArgsConstructor
public class OAuth2MemberService {

    private final MemberRepository memberRepository;


    public void register(String registrationId, ProviderUser providerUser) {
        Member member = Member.builder()
                .email(providerUser.getEmail())
                .nickname(getNickname(providerUser.getEmail()))
                .encodedPassword(UUID.randomUUID().toString().substring(0,10))
                .picture(providerUser.getPicture())
                .socialType(SocialType.valueOf(registrationId.toUpperCase()))
                .build();

         memberRepository.save(member);
    }

    private String getNickname(String email) {
        int idx = email.indexOf("@");
        return email.substring(0, idx);
    }
}
