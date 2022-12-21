package com.example.villagerservice.config.security.oauth2.converters;


import com.example.villagerservice.config.security.oauth2.model.ProviderUser;
import com.example.villagerservice.config.security.oauth2.model.social.NaverUser;
import com.example.villagerservice.config.security.oauth2.utils.OAuth2Utils;

import static com.example.villagerservice.config.security.oauth2.enums.SocialType.NAVER;

public class OAuth2NaverProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {
        if (!providerUserRequest.getClientRegistration().getRegistrationId()
                .equals(NAVER.getSocialName())) {
            return null;
        }


        return new NaverUser(OAuth2Utils.getSubAttributes(providerUserRequest.getOAuth2User(), "response"),
                providerUserRequest.getOAuth2User(),
                providerUserRequest.getClientRegistration());
    }
}
