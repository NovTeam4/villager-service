package com.example.villagerservice.party.service.impl;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyComment;
import com.example.villagerservice.party.domain.PartyTag;
import com.example.villagerservice.party.dto.PartyCommentDTO;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.exception.PartyCommentErrorCode;
import com.example.villagerservice.party.exception.PartyCommentException;
import com.example.villagerservice.party.repository.PartyCommentRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

        PartyDTO.Request request = createRequest();

        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("홍길동")
                .build();

        Party party = Party.createParty(request , member);

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

        PartyDTO.Request request = createRequest();

        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("홍길동")
                .build();

        Party party = Party.createParty(request , member);

        given(partyRepository.findById(anyLong()))
                .willReturn(Optional.of(party));

        ArgumentCaptor<PartyComment> captor = ArgumentCaptor.forClass(PartyComment.class);

        partyCommentService.createComment(partyId , "test");

        verify(partyCommentRepository,times(1)).save(captor.capture());
        Assertions.assertThat(captor.getValue().getContents()).isEqualTo("test");
    }

    @Test
    @DisplayName("모임 댓글 변경 시 , 댓글이 없을 경우")
    void updateCommentWithoutComment() {

        Long partyCommentId = 1L;
        String value = "update-test";

        PartyCommentException partyCommentException = assertThrows(PartyCommentException.class, () -> {
            partyCommentService.updateComment(anyLong() , value);
        });

        assertThat(partyCommentException.getErrorCode()).isEqualTo(PartyCommentErrorCode.COMMENT_NOT_FOUND.getErrorCode());
        assertThat(partyCommentException.getErrorMessage()).isEqualTo(PartyCommentErrorCode.COMMENT_NOT_FOUND.getErrorMessage());

    }

    @Test
    @DisplayName("모임 댓글 변경 테스트")
    void updateComment() {

        PartyDTO.Request request = createRequest();

        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("홍길동")
                .build();

        Party party = Party.createParty(request , member);

        PartyComment partyComment = PartyComment.createPartyComment("test", party);

        given(partyCommentRepository.findById(1L))
                .willReturn(Optional.of(partyComment));

        partyCommentService.updateComment(1L, "update-test");

        Assertions.assertThat(partyCommentRepository.findById(1L).get().getContents()).isEqualTo("update-test");


    }

    private static PartyDTO.Request createRequest() {
        List<PartyTag> tagList = new ArrayList<>();

        tagList.add(PartyTag.builder()
                .tagName("낚시")
                .build());
        tagList.add(PartyTag.builder()
                .tagName("볼링")
                .build());

        PartyDTO.Request request = PartyDTO.Request.builder()
                .partyName("test-party")
                .score(100)
                .startDt(LocalDate.now())
                .endDt(LocalDate.now().plusDays(2))
                .amount(1000)
                .numberPeople(2)
                .location("수원시")
                .latitude(127.1)
                .longitude(127.1)
                .content("test")
                .tagList(tagList)
                .build();

        return request;
    }
}
