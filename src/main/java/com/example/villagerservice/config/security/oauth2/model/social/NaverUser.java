package com.example.villagerservice.config.security.oauth2.model.social;

import com.example.villagerservice.config.security.oauth2.model.Attributes;
import com.example.villagerservice.config.security.oauth2.model.OAuth2ProviderUser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class NaverUser extends OAuth2ProviderUser {

    public NaverUser(Attributes mainAttributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(mainAttributes.getSubAttributes(), oAuth2User, clientRegistration);
    }

    @Override
    public String getId() {
        return (String) getAttributes().get("id");
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("name");
    }

    @Override
    public String getPicture() {
        return (String) getAttributes().get("profile_image");
    }
}
