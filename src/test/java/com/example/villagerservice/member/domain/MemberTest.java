package com.example.villagerservice.member.domain;

import com.example.villagerservice.member.exception.MemberException;
import com.example.villagerservice.member.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_DUPLICATE_ERROR;
import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_VALID_NOT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private Member member;

    @Test
    @DisplayName("비밀번호 검증 시 넘어온 비밀번호가 없을 경우 테스트")
    void checkPasswordValidNotPasswordTest() {
        // give
        // when
        MemberException memberException = assertThrows(MemberException.class, () ->
                member.checkPasswordValid(passwordEncoder, ""));

        // then
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_VALID_NOT);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_VALID_NOT.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_VALID_NOT.getErrorMessage());
    }

    @Test
    @DisplayName("비밀번호 검증 테스트")
    void checkPasswordValidTest() {
        // given
        Member member = Member.builder().encodedPassword("hello").build();

        doReturn(true)
                .when(passwordEncoder)
                .matches(anyString(), anyString());

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // when
        boolean result = member.checkPasswordValid(passwordEncoder, "ajsidf@n");

        // then
        verify(passwordEncoder, times(1)).matches(
                captor.capture(), anyString());
        assertThat(result).isTrue();
        assertThat(captor.getValue()).isEqualTo("ajsidf@n");
    }
}