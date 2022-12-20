package com.example.villagerservice.config.security.oauth2.converters;


import com.example.villagerservice.config.security.oauth2.model.ProviderUser;
import com.example.villagerservice.config.security.oauth2.model.social.GoogleUser;
import com.example.villagerservice.config.security.oauth2.utils.OAuth2Utils;

import static com.example.villagerservice.config.security.oauth2.enums.SocialType.GOOGLE;


public class OAuth2GoogleProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        if(!providerUserRequest.getClientRegistration().getRegistrationId()
                .equals(GOOGLE.getSocialName())) {
            return null;
        }


        return new GoogleUser(OAuth2Utils.getMainAttributes(providerUserRequest.getOAuth2User()),
                providerUserRequest.getOAuth2User(),
                providerUserRequest.getClientRegistration());
    }
}
