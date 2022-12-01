package com.example.villagerservice.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtTokenInfoDto {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenValidTime;
    private Long refreshTokenExpirationTime;
}