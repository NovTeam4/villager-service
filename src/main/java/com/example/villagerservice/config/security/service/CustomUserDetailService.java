package com.example.villagerservice.config.security.service;

import com.example.villagerservice.common.exception.AuthVillagerException;
import com.example.villagerservice.config.security.context.MemberContext;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

import static com.example.villagerservice.common.exception.AuthErrorCode.AUTH_MEMBER_ALREADY_DELETED;
import static com.example.villagerservice.common.exception.AuthErrorCode.AUTH_INFO_NOT_VALID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByMemberDetail(username)
                .orElseThrow(() -> new AuthVillagerException(AUTH_INFO_NOT_VALID));

        if (member.isDeleted()) {
            throw new AuthVillagerException(AUTH_MEMBER_ALREADY_DELETED);
        }

        List<GrantedAuthority> collect = new LinkedList<>();
        collect.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new MemberContext(member, collect);
    }
}
