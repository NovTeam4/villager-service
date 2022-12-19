package com.example.villagerservice.config.security.oauth2.service;

import com.example.villagerservice.config.security.oauth2.converters.ProviderUserConverter;
import com.example.villagerservice.config.security.oauth2.converters.ProviderUserRequest;
import com.example.villagerservice.config.security.oauth2.model.ProviderUser;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Getter
@Service
@RequiredArgsConstructor
public abstract class AbstractOAuth2UserService {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter;

    public void register(ProviderUser providerUser, OAuth2UserRequest userRequest) {
        Optional<Member> member = memberRepository.findByEmail(providerUser.getEmail());
        if(member.isEmpty()) {
            memberService.register(userRequest.getClientRegistration().getRegistrationId(), providerUser);
        } else {
            System.out.println("member = " + member);
        }
    }

    protected ProviderUser providerUser(ProviderUserRequest providerUserRequest) {
        return providerUserConverter.converter(providerUserRequest);
    }
}
