package com.example.villagerservice.member.service;

import com.example.villagerservice.common.jwt.JwtTokenInfoDto;

public interface AuthTokenService {
    JwtTokenInfoDto getReissueTokenInfo(String accessToken, String refreshToken);
}
