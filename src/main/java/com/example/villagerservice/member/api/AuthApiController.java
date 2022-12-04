package com.example.villagerservice.member.api;

import com.example.villagerservice.common.jwt.JwtTokenException;
import com.example.villagerservice.common.jwt.JwtTokenInfoDto;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.request.MemberCreate;
import com.example.villagerservice.member.service.AuthTokenService;
import com.example.villagerservice.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.villagerservice.common.jwt.JwtTokenErrorCode.JWT_ACCESS_TOKEN_NOT_EXIST;
import static com.example.villagerservice.common.jwt.JwtTokenErrorCode.JWT_REFRESH_TOKEN_NOT_EXIST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthApiController {

    private final MemberService memberService;
    private final AuthTokenService authTokenService;

    @PostMapping("/signup")
    public void createMember(@Valid @RequestBody MemberCreate memberCreate) {
        memberService.createMember(memberCreate);
    }

    @PostMapping("/refresh")
    public JwtTokenResponse reissue(
            @AuthenticationPrincipal Member member,
            @RequestHeader("access-token") String accessToken,
            @RequestHeader("refresh-token") String refreshToken) {
        if (!StringUtils.hasText(accessToken)) {
            throw new JwtTokenException(JWT_ACCESS_TOKEN_NOT_EXIST);
        }
        if (!StringUtils.hasText(refreshToken)) {
            throw new JwtTokenException(JWT_REFRESH_TOKEN_NOT_EXIST);
        }

        JwtTokenInfoDto jwtTokenInfoDto = authTokenService.getReissueTokenInfo(member.getEmail(), accessToken, refreshToken);
        return JwtTokenResponse.createResponseBody(jwtTokenInfoDto);
    }
}
