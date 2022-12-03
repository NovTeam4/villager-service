package com.example.villagerservice.member.service.impl;

import com.example.villagerservice.common.jwt.JwtTokenException;
import com.example.villagerservice.common.jwt.JwtTokenProvider;
import com.example.villagerservice.common.jwt.JwtTokenInfoDto;
import com.example.villagerservice.config.redis.RedisRepository;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.service.AuthTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.example.villagerservice.common.jwt.JwtTokenErrorCode.JWT_REFRESH_TOKEN_NOT_SAME;
import static com.example.villagerservice.common.jwt.JwtTokenType.ACCESS_TOKEN;
import static com.example.villagerservice.common.jwt.JwtTokenType.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisRepository redisRepository;

    @Override
    public JwtTokenInfoDto getReissueTokenInfo(String accessToken, String refreshToken) {
        jwtTokenProvider.validateToken(accessToken, ACCESS_TOKEN);
        jwtTokenProvider.validateToken(refreshToken, REFRESH_TOKEN);

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        String findRefreshToken = redisRepository.findRefreshToken(authentication);
        if (!refreshToken.equals(findRefreshToken)) {
            throw new JwtTokenException(JWT_REFRESH_TOKEN_NOT_SAME);
        }

        JwtTokenInfoDto jwtTokenInfoDto = jwtTokenProvider.generateToken(authentication);
        redisRepository.saveRefreshToken(authentication, jwtTokenInfoDto);

        return jwtTokenInfoDto;
    }
}
