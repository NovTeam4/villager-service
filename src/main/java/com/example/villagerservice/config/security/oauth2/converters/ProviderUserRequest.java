package com.example.villagerservice.config.security.oauth2.converters;

import com.example.villagerservice.member.domain.Member;
import lombok.Getter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class ProviderUserRequest {
    private ClientRegistration clientRegistration;
    private OAuth2User oAuth2User;
    private Member member;

    public ProviderUserRequest(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        this.clientRegistration = clientRegistration;
        this.oAuth2User = oAuth2User;
    }

    public ProviderUserRequest(Member member) {
        this.member = member;
    }
}
