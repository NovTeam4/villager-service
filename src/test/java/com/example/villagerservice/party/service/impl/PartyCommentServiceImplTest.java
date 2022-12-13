package com.example.villagerservice.party.service.impl;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyComment;
import com.example.villagerservice.party.exception.PartyCommentErrorCode;
import com.example.villagerservice.party.exception.PartyCommentException;
import com.example.villagerservice.party.repository.PartyCommentRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PartyCommentServiceImplTest {

    @Mock
    PartyRepository partyRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PartyCommentRepository partyCommentRepository;

    @InjectMocks
    PartyCommentServiceImpl partyCommentService;

    @Test
    @DisplayName("모임 댓글 등록 시 , 내용이 없을 경우")
    void createCommentWithoutContent() {

        Member member = Member.builder()
                .email("testparty@gmail.com")
                .nickname("홍길동")
                .build();

        Party party = Party.createParty(
                "test-party",
                100,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                1000,
                member
        );

        given(partyRepository.findById(anyLong()))
                .willReturn(Optional.of(party));

        PartyCommentException partyCommentException = assertThrows(PartyCommentException.class, () -> {
            partyCommentService.createComment(anyLong() , "");
        });

        assertThat(partyCommentException.getErrorCode()).isEqualTo(PartyCommentErrorCode.CONTENT_IS_REQUIRED.getErrorCode());
        assertThat(partyCommentException.getErrorMessage()).isEqualTo(PartyCommentErrorCode.CONTENT_IS_REQUIRED.getErrorMessage());

    }

    @Test
    @DisplayName("모임 댓글 등록 테스트")
    void createComment() {

        Long partyId = 1L;

        Member member = Member.builder()
                .email("testparty@gmail.com")
                .nickname("홍길동")
                .build();

        Party party = Party.createParty(
                "test-party",
                100,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                1000,
                member
        );

        given(partyRepository.findById(anyLong()))
                .willReturn(Optional.of(party));

        ArgumentCaptor<PartyComment> captor = ArgumentCaptor.forClass(PartyComment.class);

        partyCommentService.createComment(partyId , "test");

        verify(partyCommentRepository,times(1)).save(captor.capture());
        Assertions.assertThat(captor.getValue().getContents()).isEqualTo("test");
    }
}
