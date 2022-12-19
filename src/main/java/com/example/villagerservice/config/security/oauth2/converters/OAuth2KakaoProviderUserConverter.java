package com.example.villagerservice.config.security.oauth2.converters;

import com.example.villagerservice.config.security.oauth2.model.ProviderUser;
import com.example.villagerservice.config.security.oauth2.model.social.KakaoUser;
import com.example.villagerservice.config.security.oauth2.utils.OAuth2Utils;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import static com.example.villagerservice.config.security.oauth2.enums.SocialType.KAKAO;

public class OAuth2KakaoProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {
        if(!providerUserRequest.getClientRegistration().getRegistrationId()
                .equals(KAKAO.getSocialName())) {
            return null;
        }

        if(providerUserRequest.getOAuth2User() instanceof OidcUser) {
            return null;
        }

        return new KakaoUser(OAuth2Utils.getOtherAttributes(providerUserRequest.getOAuth2User(), "kakao_account", "profile"),
                providerUserRequest.getOAuth2User(),
                providerUserRequest.getClientRegistration());
    }
}
