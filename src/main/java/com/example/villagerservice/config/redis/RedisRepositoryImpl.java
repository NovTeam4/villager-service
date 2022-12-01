package com.example.villagerservice.config.redis;

import com.example.villagerservice.common.jwt.JwtTokenInfoDto;
import com.example.villagerservice.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REDIS_KEY_PREFIX = "VILLAGER:";

    @Override
    public void saveRefreshToken(Authentication authentication, JwtTokenInfoDto value) {
        redisTemplate.opsForValue()
                .set(getKey(authentication), value.getRefreshToken(), value.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
    }

    @Override
    public String findRefreshToken(Authentication authentication) {
        return (String) redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + ((Member) authentication.getPrincipal()).getEmail());
    }

    private String getKey(Authentication authentication) {
        return REDIS_KEY_PREFIX + ((Member) authentication.getPrincipal()).getEmail();
    }
}
