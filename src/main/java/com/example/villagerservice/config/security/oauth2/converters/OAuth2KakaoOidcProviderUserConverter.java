package com.example.villagerservice.config.security.oauth2.converters;

import com.example.villagerservice.config.security.oauth2.model.ProviderUser;
import com.example.villagerservice.config.security.oauth2.model.social.KakaoOidcUser;
import com.example.villagerservice.config.security.oauth2.utils.OAuth2Utils;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import static com.example.villagerservice.config.security.oauth2.enums.SocialType.KAKAO;

public class OAuth2KakaoOidcProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {
        if (!providerUserRequest.getClientRegistration().getRegistrationId()
                .equals(KAKAO.getSocialName())) {
            return null;
        }

        if (!(providerUserRequest.getOAuth2User() instanceof OidcUser)) {
            return null;
        }


        return new KakaoOidcUser(
                OAuth2Utils.getMainAttributes(providerUserRequest.getOAuth2User()),
                providerUserRequest.getOAuth2User(),
                providerUserRequest.getClientRegistration());
    }
}
