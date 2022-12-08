package com.example.villagerservice.member.api;

import com.example.villagerservice.common.jwt.JwtTokenException;
import com.example.villagerservice.common.jwt.JwtTokenInfoDto;
import com.example.villagerservice.common.jwt.JwtTokenProvider;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.dto.CreateMember;
import com.example.villagerservice.member.dto.ValidMemberNickname;
import com.example.villagerservice.member.service.AuthTokenService;
import com.example.villagerservice.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.example.villagerservice.common.jwt.JwtTokenErrorCode.JWT_ACCESS_TOKEN_NOT_EXIST;
import static com.example.villagerservice.common.jwt.JwtTokenErrorCode.JWT_REFRESH_TOKEN_NOT_EXIST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthApiController {

    private final MemberService memberService;
    private final AuthTokenService authTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public void createMember(@Valid @RequestBody CreateMember.Request createMember) {
        memberService.createMember(createMember);
    }

    @PostMapping("/valid/nickname")
    public void validNickname(@Valid @RequestBody ValidMemberNickname.Request request) {
        memberService.validNickname(request);
    }

    @PostMapping("/refresh")
    public JwtTokenResponse reissue(
            @AuthenticationPrincipal Member member,
            HttpServletRequest request) {

        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        if (!StringUtils.hasText(accessToken)) {
            throw new JwtTokenException(JWT_ACCESS_TOKEN_NOT_EXIST);
        }
        if (!StringUtils.hasText(refreshToken)) {
            throw new JwtTokenException(JWT_REFRESH_TOKEN_NOT_EXIST);
        }

        JwtTokenInfoDto jwtTokenInfoDto = authTokenService.getReissueTokenInfo(
                member.getId(), accessToken, refreshToken);
        return JwtTokenResponse.createResponseBody(jwtTokenInfoDto);
    }
}
