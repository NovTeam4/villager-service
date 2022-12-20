package com.example.villagerservice.config.security.oauth2.model.social;

import com.example.villagerservice.config.security.oauth2.model.Attributes;
import com.example.villagerservice.config.security.oauth2.model.OAuth2ProviderUser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class KakaoOidcUser extends OAuth2ProviderUser {

    private final Map<String, Object> otherAttributes;

    public KakaoOidcUser(Attributes mainAttributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(mainAttributes.getMainAttributes(), oAuth2User, clientRegistration);
        this.otherAttributes = mainAttributes.getOtherAttributes();
    }

    @Override
    public String getId() {
        return (String) getAttributes().get("id");
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("email");
    }

    @Override
    public String getPicture() {
        return (String) getAttributes().get("profile_image_url");
    }
}
