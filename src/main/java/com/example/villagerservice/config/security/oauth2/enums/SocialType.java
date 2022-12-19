package com.example.villagerservice.config.security.oauth2.enums;

import lombok.Getter;

@Getter
public enum SocialType {
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao"),
    VILLAGER("villager")
    ;

    private final String socialName;

    SocialType(String socialName) {
        this.socialName = socialName;
    }
}