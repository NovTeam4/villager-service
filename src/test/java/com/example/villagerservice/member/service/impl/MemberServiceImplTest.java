package com.example.villagerservice.member.service.impl;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.exception.MemberException;
import com.example.villagerservice.member.repository.MemberRepository;
import com.example.villagerservice.member.request.MemberCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_DUPLICATE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;
    
    @Test
    @DisplayName("회원 가입 시 이메일 중복 검사 테스트")
    void createMemberDuplicateTest() {
        // given
        String nickname = "테스트입니다.";
        String email = "test@gmail.com";
        String pass = "123456789";

        MemberCreate memberCreate = createMemberRequest(nickname, email, pass);
        Member member = createMember(email, nickname, pass);

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // when
        MemberException memberException = assertThrows(MemberException.class, () -> {
            memberService.createMember(memberCreate);
        });

        // then
        verify(memberRepository, times(1)).findByEmail(captor.capture());
        assertThat(captor.getValue()).isEqualTo(email);
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_DUPLICATE_ERROR);
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void createMemberTest() {
        // given
        String nickname = "테스트입니다.";
        String email = "test@gmail.com";
        String pass = "123456789";

        MemberCreate memberCreate = createMemberRequest(nickname, email, pass);
        Member member = createMember(email, nickname, pass);

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

        // when
        memberService.createMember(memberCreate);

        // then
        verify(memberRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getNickname()).isEqualTo(nickname);
        assertThat(captor.getValue().getEmail()).isEqualTo(email);
        assertThat(captor.getValue().getPass()).isEqualTo(pass);
    }

    private Member createMember(String nickname, String email, String pass) {
        return Member.builder()
                .nickname(nickname)
                .email(email)
                .pass(pass)
                .build();
    }

    private MemberCreate createMemberRequest(String nickname, String email, String pass) {
        return MemberCreate.builder()
                .nickname(nickname)
                .email(email)
                .password(pass)
                .build();
    }
}