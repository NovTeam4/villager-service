package com.example.villagerservice.party.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyList;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.repository.PartyListRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.request.PartyCreate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PartyListServiceImplTest {
    @Mock
    private PartyListRepository partyListRepository;

    @Mock
    private PartyRepository partyRepository;

    @InjectMocks
    private PartyListServiceImpl partyListService;

    @Test
    @DisplayName("모임 신청 성공 테스트")
    void partyApplyTest() {
        // given
        Long partyId = 1L;

        PartyDTO.Request partyRequest = PartyDTO.Request.builder()
                .partyName("재밌는모임")
                .score(100)
                .startDt(LocalDateTime.now())
                .endDt(LocalDateTime.now().plusHours(2))
                .amount(1000)
                .build();

        Member member = Member.builder()
                .nickname("홍길동")
                .encodedPassword("1234")
                .email("123@123")
                .build();

        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.of(Party.builder()
                            .partyName(partyRequest.getPartyName())
                            .score(partyRequest.getScore())
                            .startDt(partyRequest.getStartDt())
                            .endDt(partyRequest.getEndDt())
                            .amount(partyRequest.getAmount())
                            .member(member)
                    .build()));

        given(partyRepository.save(any()))
            .willReturn(Optional.of(Party.builder()
                    .partyName(partyRequest.getPartyName())
                    .score(partyRequest.getScore())
                    .startDt(partyRequest.getStartDt())
                    .endDt(partyRequest.getEndDt())
                    .amount(partyRequest.getAmount())
                    .member(member)
                    .build()));

        ArgumentCaptor<PartyList> captor = ArgumentCaptor.forClass(PartyList.class);

        // when
        partyListService.applyParty(member.getEmail(), partyId);

        // then
        verify(partyListRepository, times(1)).save(captor.capture());
        assertEquals(1L, captor.getValue().getId());
    }

    @Test
    @DisplayName("모임 신청 실패 - 이미 신청한 모임")
    void partyApplyTestFailedAlreadyApplied() {
        // given
        Long partyId = 1L;

        PartyDTO.Request partyRequest = PartyDTO.Request.builder()
                .partyName("재밌는모임")
                .score(100)
                .startDt(LocalDateTime.now())
                .endDt(LocalDateTime.now().plusHours(2))
                .amount(1000)
                .build();

        Member member = Member.builder()
            .nickname("홍길동")
            .encodedPassword("1234")
            .email("123@123")
            .build();

        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.of(Party.builder()
                    .partyName(partyRequest.getPartyName())
                    .score(partyRequest.getScore())
                    .startDt(partyRequest.getStartDt())
                    .endDt(partyRequest.getEndDt())
                    .amount(partyRequest.getAmount())
                    .member(member)
                    .build()));

        given(partyRepository.save(any()))
            .willReturn(Optional.of(Party.builder()
                    .partyName(partyRequest.getPartyName())
                    .score(partyRequest.getScore())
                    .startDt(partyRequest.getStartDt())
                    .endDt(partyRequest.getEndDt())
                    .amount(partyRequest.getAmount())
                    .member(member)
                    .build()));

        ArgumentCaptor<PartyList> captor = ArgumentCaptor.forClass(PartyList.class);

        // when
        partyListService.applyParty(member.getEmail(), partyId);

        // then
        verify(partyListRepository, times(1)).save(captor.capture());
        assertEquals(1L, captor.getValue().getId());
    }
}