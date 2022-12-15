package com.example.villagerservice.config.security.handler;

import com.example.villagerservice.common.jwt.JwtTokenInfoDto;
import com.example.villagerservice.common.jwt.JwtTokenProvider;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.redis.RedisRepository;
import com.example.villagerservice.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisRepository redisRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            JwtTokenInfoDto jwtTokenInfoDto = jwtTokenProvider.generateToken(authentication);

            redisRepository.saveRefreshToken(authentication, jwtTokenInfoDto);
            JwtTokenResponse.createJwtTokenResponse(response, jwtTokenInfoDto, getLoginMemberId(authentication));
        } catch (Exception e) {
            log.error("error : ", e);
        }
    }

    private Long getLoginMemberId(Authentication authentication) {
        return ((Member) authentication.getPrincipal()).getId();
    }
}
