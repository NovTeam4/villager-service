package com.example.villagerservice.common.jwt;

import com.example.villagerservice.member.domain.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.villagerservice.common.jwt.JwtTokenErrorCode.*;
import static com.example.villagerservice.common.jwt.JwtTokenErrorCode.JWT_INVALID_TOKEN;

@Slf4j
@Getter
@Component
public class JwtTokenProvider {
    private final Key key;
    private final String JWT_TOKEN_GRANT_TYPE;
    private final String JWT_TOKEN_HEADER;
    private final String accessTokenResponseHeaderName;
    private final String refreshTokenResponseHeaderName;
    private final Long accessTokenValidTime;
    private final Long refreshTokenValidTime;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.access-token-validity-in-seconds}") Long accessTokenValidTime,
                            @Value("${jwt.refresh-token-validity-in-seconds}") Long refreshTokenValidTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.JWT_TOKEN_GRANT_TYPE = "Bearer";
        this.JWT_TOKEN_HEADER = "Authorization";
        this.accessTokenResponseHeaderName = "access-token";
        this.refreshTokenResponseHeaderName = "refresh-token";
        this.accessTokenValidTime = accessTokenValidTime;
        this.refreshTokenValidTime = refreshTokenValidTime;
    }

    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public JwtTokenInfoDto generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(((Member) authentication.getPrincipal()).getEmail())
                .claim("auth", authorities)
                .addClaims(createClaims((Member) authentication.getPrincipal()))
                .setExpiration(new Date(now + accessTokenValidTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + refreshTokenValidTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtTokenInfoDto.builder()
                .grantType(JWT_TOKEN_GRANT_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenValidTime(accessTokenValidTime)
                .refreshTokenExpirationTime(refreshTokenValidTime)
                .build();
    }

    private Map<String, Object> createClaims(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", member.getEmail());
        claims.put("nickname", member.getNickname());
        return claims;
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {

        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(Member.builder()
                .email(claims.getSubject())
                .nickname((String) claims.get("nickname"))
                .build(), "", authorities);
    }

    public void validateAccessToken(String accessToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new JwtTokenException(JWT_ACCESS_TOKEN_MALFORMED_ERROR); // 잘못된 JWT 서명
        } catch (ExpiredJwtException e) {
            throw new JwtTokenException(JWT_ACCESS_TOKEN_EXPIRED); // 만료된 JWT 토큰
        } catch (UnsupportedJwtException e) {
            throw new JwtTokenException(JWT_UNSUPPORTED_TOKEN); // 지원되지 않는 JWT 토큰
        } catch (IllegalArgumentException e) {
            throw new JwtTokenException(JWT_CLAIMS_EMPTY); // JWT 토큰이 잘못
        } catch (Exception e) {
            throw new JwtTokenException(JWT_INVALID_TOKEN);
        }
    }

    public void validateToken(String token, JwtTokenType tokenType) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (ExpiredJwtException ignored) {

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            if (tokenType == JwtTokenType.ACCESS_TOKEN) {
                throw new JwtTokenException(JWT_ACCESS_TOKEN_MALFORMED_ERROR); // 잘못된 JWT 서명
            } else {
                throw new JwtTokenException(JWT_REFRESH_TOKEN_MALFORMED_ERROR); // 잘못된 JWT 서명
            }
        } catch (UnsupportedJwtException e) {
            throw new JwtTokenException(JWT_UNSUPPORTED_TOKEN); // 지원되지 않는 JWT 토큰
        } catch (IllegalArgumentException e) {
            throw new JwtTokenException(JWT_CLAIMS_EMPTY); // JWT 토큰이 잘못
        } catch (Exception e) {
            throw new JwtTokenException(JWT_INVALID_TOKEN);
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JWT_TOKEN_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JWT_TOKEN_GRANT_TYPE)) {
            String token = bearerToken.substring(7);
            validateAccessToken(token);
            return token;
        } else {
            throw new IllegalArgumentException("유효한 JWT 토큰이 없습니다.");
        }
    }
}