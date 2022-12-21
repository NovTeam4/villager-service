package com.example.villagerservice.config.redis;

import com.example.villagerservice.common.jwt.JwtTokenInfoDto;
import org.springframework.security.core.Authentication;

public interface RedisRepository {
    void saveRefreshToken(Authentication authentication, JwtTokenInfoDto value);
    String findRefreshToken(Authentication authentication);
    void deleteRefreshToken(Authentication authentication);
}
