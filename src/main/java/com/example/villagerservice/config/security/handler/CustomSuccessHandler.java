package com.example.villagerservice.config.security.handler;

import com.example.villagerservice.common.jwt.JwtTokenErrorCode;
import com.example.villagerservice.common.jwt.JwtTokenInfoDto;
import com.example.villagerservice.common.jwt.JwtTokenProvider;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.redis.RedisRepository;
import com.example.villagerservice.config.security.oauth2.CookieUtils;
import com.example.villagerservice.config.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.villagerservice.config.security.oauth2.model.PrincipalUser;
import com.example.villagerservice.config.security.properties.OAuth2Properties;
import com.example.villagerservice.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.example.villagerservice.config.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisRepository redisRepository;
    private final OAuth2Properties oauth2Properties;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            JwtTokenInfoDto jwtTokenInfoDto = null;
            if (authentication.getPrincipal() instanceof Member) {
                jwtTokenInfoDto = jwtTokenProvider.generateToken(authentication);
                if (jwtTokenInfoDto != null) {
                    redisRepository.saveRefreshToken(authentication, jwtTokenInfoDto);
                    JwtTokenResponse.createJwtTokenResponse(response, jwtTokenInfoDto, getLoginMemberId(authentication));
                } else {
                    JwtTokenResponse.createJwtTokenErrorResponse(response, 400, JwtTokenErrorCode.JWT_INVALID_TOKEN);
                }
            } else {
                PrincipalUser user = (PrincipalUser) authentication.getPrincipal();
                jwtTokenInfoDto = jwtTokenProvider.generateToken(user);
                if (jwtTokenInfoDto != null) {
                    String targetUrl = determineTargetUrl(request, response, authentication, jwtTokenInfoDto, user.getMemberId());
                    redisRepository.saveRefreshToken(authentication, jwtTokenInfoDto);
                    clearAuthenticationAttributes(request, response);
                    getRedirectStrategy().sendRedirect(request, response, targetUrl);
                } else {
                    JwtTokenResponse.createJwtTokenErrorResponse(response, 400, JwtTokenErrorCode.JWT_INVALID_TOKEN);
                }
            }
        } catch (Exception e) {
            log.error("error : ", e);
        }
    }

    private Long getLoginMemberId(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Member) {
            return ((Member) authentication.getPrincipal()).getId();
        } else {
            return ((PrincipalUser) authentication.getPrincipal()).getMemberId();
        }
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication, JwtTokenInfoDto jwtTokenInfoDto, Long loginMemberId) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            try {
                JwtTokenResponse.createJwtTokenErrorResponse(response, 400, JwtTokenErrorCode.UNAUTHORIZED_REDIRECT_URI);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", jwtTokenInfoDto.getAccessToken())
                .queryParam("refreshToken", jwtTokenInfoDto.getRefreshToken())
                .queryParam("grantType", "Bearer")
                .queryParam("loginMemberId", loginMemberId)
                .queryParam("accessTokenExpirationTime", jwtTokenInfoDto.getAccessTokenValidTime())
                .build()
                .toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return oauth2Properties.getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
}
