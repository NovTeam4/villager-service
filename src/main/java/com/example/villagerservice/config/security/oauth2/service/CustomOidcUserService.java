package com.example.villagerservice.config.security.oauth2.service;

import com.example.villagerservice.config.security.oauth2.converters.ProviderUserConverter;
import com.example.villagerservice.config.security.oauth2.converters.ProviderUserRequest;
import com.example.villagerservice.config.security.oauth2.model.PrincipalUser;
import com.example.villagerservice.config.security.oauth2.model.ProviderUser;
import com.example.villagerservice.member.domain.MemberRepository;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends AbstractOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    public CustomOidcUserService(MemberRepository memberRepository, ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter) {
        super(memberRepository, providerUserConverter);
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = ClientRegistration.withClientRegistration(userRequest.getClientRegistration()).userNameAttributeName("sub").build();

        OidcUserRequest oidcUserRequest = new OidcUserRequest(clientRegistration, userRequest.getAccessToken(), userRequest.getIdToken(), userRequest.getAdditionalParameters());

        OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService = new OidcUserService();
        OidcUser oidcUser = oidcUserService.loadUser(oidcUserRequest);
        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration, oidcUser);

        ProviderUser providerUser = super.providerUser(providerUserRequest);

        // 회원 가입
        Long memberId = super.register(providerUser, userRequest);

        return new PrincipalUser(providerUser, memberId);
    }
}
