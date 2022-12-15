package com.example.villagerservice.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.villagerservice.common.jwt.JwtTokenErrorCode.createErrorResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtTokenResponse {

    private String accessToken;
    private String grantType;
    private String refreshToken;
    private Long accessTokenExpirationTime;
    private Long loginMemberId;

    public static void createJwtTokenResponse(HttpServletResponse response, JwtTokenInfoDto jwtTokenInfoDto, Long loginMemberId) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        new ObjectMapper().writeValue(response.getWriter(), createResponseBody(jwtTokenInfoDto, loginMemberId));
    }

    public static void createJwtTokenErrorResponse(HttpServletResponse response, int status, JwtTokenErrorCode jwtTokenErrorCode) throws IOException {
        response.setStatus(status);
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        new ObjectMapper().writeValue(response.getWriter(), createErrorResponse(jwtTokenErrorCode));
    }

    public static JwtTokenResponse createResponseBody(JwtTokenInfoDto jwtTokenInfoDto, Long loginMemberId) {
        return JwtTokenResponse.builder()
                .accessToken(jwtTokenInfoDto.getAccessToken())
                .grantType(jwtTokenInfoDto.getGrantType())
                .refreshToken(jwtTokenInfoDto.getRefreshToken())
                .accessTokenExpirationTime(jwtTokenInfoDto.getAccessTokenValidTime())
                .loginMemberId(loginMemberId)
                .build();
    }
}
