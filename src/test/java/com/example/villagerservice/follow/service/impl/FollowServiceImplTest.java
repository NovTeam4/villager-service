package com.example.villagerservice.follow.service.impl;

import com.example.villagerservice.follow.domain.Follow;
import com.example.villagerservice.follow.domain.FollowRepository;
import com.example.villagerservice.follow.exception.FollowException;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static com.example.villagerservice.follow.exception.FollowErrorCode.FOLLOW_ALREADY_STATUS;
import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceImplTest {
    @Mock
    private FollowRepository followRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private FollowServiceImpl followService;

    @Test
    @DisplayName("팔로우 신청 시 팔로우 신청자가 존재하지 않을 경우")
    void followFromMemberNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> followService.following(1L, anyLong()));

        // then
        verify(memberRepository, times(1)).findById(anyLong());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_NOT_FOUND);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("팔로우 신청 시 팔로우가 존재하지 않을 경우")
    void followToMemberNotFoundTest() {
        // given
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(Member.builder().build()))
                .thenReturn(Optional.empty());

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> followService.following(1L, 1L));

        // then
        verify(memberRepository, times(2)).findById(anyLong());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_NOT_FOUND);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_FOUND.getErrorMessage());
    }
    
    @Test
    @DisplayName("팔로우가 이미된 상태 테스트")
    void followAlreadyStatusTest() {
        // given
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(Member.builder().build()))
                .thenReturn(Optional.of(Member.builder().build()));

        given(followRepository.existsByFromMemberAndToMember(any(), any()))
                .willReturn(true);

        // when
        FollowException followException = assertThrows(FollowException.class,
                () -> followService.following(1L, 1L));

        // then
        verify(memberRepository, times(2)).findById(anyLong());
        verify(followRepository, times(1)).existsByFromMemberAndToMember(any(), any());
        assertThat(followException.getMemberErrorCode()).isEqualTo(FOLLOW_ALREADY_STATUS);
        assertThat(followException.getErrorCode()).isEqualTo(FOLLOW_ALREADY_STATUS.getErrorCode());
        assertThat(followException.getErrorMessage()).isEqualTo(FOLLOW_ALREADY_STATUS.getErrorMessage());
    }
    
    @Test
    @DisplayName("팔로잉 테스트")
    void followingTest() {
        // given
        Member fromMember = Member.builder()
                .email("fromMember@gamil.com")
                .build();
        Member toMember = Member.builder()
                .email("toMember@gamil.com")
                .build();

        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(fromMember))
                .thenReturn(Optional.of(toMember));

        given(followRepository.existsByFromMemberAndToMember(fromMember, toMember))
                .willReturn(false);

        ArgumentCaptor<Follow> captor = ArgumentCaptor.forClass(Follow.class);

        // when
        followService.following(1L, 1L);

        // then
        verify(memberRepository, times(2)).findById(anyLong());
        verify(followRepository, times(1)).existsByFromMemberAndToMember(any(), any());
        verify(followRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getFromMember().getEmail())
                .isEqualTo("fromMember@gamil.com");
        assertThat(captor.getValue().getToMember().getEmail())
                .isEqualTo("toMember@gamil.com");
    }
}