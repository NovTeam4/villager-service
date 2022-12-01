package com.example.villagerservice.member.api;

import com.example.villagerservice.common.jwt.JwtTokenException;
import com.example.villagerservice.common.jwt.JwtTokenInfoDto;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.member.request.MemberCreate;
import com.example.villagerservice.member.service.AuthTokenService;
import com.example.villagerservice.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.villagerservice.common.jwt.JwtTokenErrorCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthApiController {

    private final MemberService memberService;
    private final AuthTokenService authTokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public void createMember(@Valid @RequestBody MemberCreate memberCreate) {
        memberCreate.passwordEncrypt(passwordEncoder.encode(memberCreate.getPassword()));
        memberService.createMember(memberCreate);
    }

    @PostMapping("/refresh")
    public JwtTokenResponse reissue(@RequestHeader("access-token") String accessToken,
                                                    @RequestHeader("refresh-token") String refreshToken) {
        if (!StringUtils.hasText(accessToken)) {
            throw new JwtTokenException(JWT_ACCESS_TOKEN_NOT_EXIST);
        }
        if (!StringUtils.hasText(refreshToken)) {
            throw new JwtTokenException(JWT_REFRESH_TOKEN_NOT_EXIST);
        }

        JwtTokenInfoDto jwtTokenInfoDto = authTokenService.getReissueTokenInfo(accessToken, refreshToken);
        return JwtTokenResponse.createResponseBody(jwtTokenInfoDto);
    }
}
