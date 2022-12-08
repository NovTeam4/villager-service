package com.example.villagerservice.config;

import com.example.villagerservice.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomMember> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomMember annotation) {
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        final Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("original")
                .build();
        member.setJwtMemberId(1L);

        final UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(member, "password", null);

        securityContext.setAuthentication(authenticationToken);
        return securityContext;
    }
}
