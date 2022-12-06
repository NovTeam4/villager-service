package com.example.villagerservice.config.security.filters;

import com.example.villagerservice.common.jwt.JwtTokenException;
import com.example.villagerservice.common.jwt.JwtTokenProvider;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.villagerservice.common.jwt.JwtTokenErrorCode.JWT_ACCESS_TOKEN_NOT_EXIST;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();

         if (isPass(servletPath)) {
            filterChain.doFilter(request, response);
         }else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
                JwtTokenResponse.createJwtTokenErrorResponse(response, SC_BAD_REQUEST, JWT_ACCESS_TOKEN_NOT_EXIST);
            } else {
                try {
                    String accessToken = jwtTokenProvider.resolveToken(request);

                    Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                    Member member = (Member) authentication.getPrincipal();
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member, null, authentication.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (JwtTokenException e) {
                    JwtTokenResponse.createJwtTokenErrorResponse(response, SC_BAD_REQUEST, e.getMemberErrorCode());
                }
            }
        }
    }

    private boolean isPass(String path) {
        return path.equals("/api/v1/auth/login") ||
               path.equals("/api/v1/auth/signup") ||
               path.equals("/h2-console") || path.contains("/docs")
                ;
    }
}