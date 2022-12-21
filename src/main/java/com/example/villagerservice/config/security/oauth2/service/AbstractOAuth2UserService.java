package com.example.villagerservice.config.security.oauth2.service;

import com.example.villagerservice.config.security.oauth2.converters.ProviderUserConverter;
import com.example.villagerservice.config.security.oauth2.converters.ProviderUserRequest;
import com.example.villagerservice.config.security.oauth2.enums.SocialType;
import com.example.villagerservice.config.security.oauth2.model.ProviderUser;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Getter
@Service
@RequiredArgsConstructor
public abstract class AbstractOAuth2UserService {
    private final MemberRepository memberRepository;
    private final ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter;

    public Long register(ProviderUser providerUser, OAuth2UserRequest userRequest) {
        Optional<Member> member = memberRepository.findByEmail(providerUser.getEmail());
        if(member.isEmpty()) {
            return register(userRequest.getClientRegistration().getRegistrationId(), providerUser);
        } else {
            return member.get().getId();
        }
    }

    protected ProviderUser providerUser(ProviderUserRequest providerUserRequest) {
        return providerUserConverter.converter(providerUserRequest);
    }

    private Long register(String registrationId, ProviderUser providerUser) {
        Member member = Member.builder()
                .email(providerUser.getEmail())
                .nickname(getNickname(providerUser.getEmail()))
                .encodedPassword(providerUser.getPassword())
                .picture(providerUser.getPicture())
                .socialType(SocialType.valueOf(registrationId.toUpperCase()))
                .build();

        return memberRepository.save(member).getId();
    }

    private String getNickname(String email) {
        int idx = email.indexOf("@");
        return email.substring(0, idx);
    }
}
