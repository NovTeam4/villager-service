package com.example.villagerservice.config.security.service;

import com.example.villagerservice.config.security.context.MemberContext;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.exception.MemberException;
import com.example.villagerservice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        List<GrantedAuthority> collect = new LinkedList<>();
        collect.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new MemberContext(member, collect);
    }
}
