package com.example.villagerservice.party.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.villagerservice.member.domain.Tag;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyApply;
import com.example.villagerservice.party.exception.PartyApplyErrorCode;
import com.example.villagerservice.party.exception.PartyApplyException;
import com.example.villagerservice.party.repository.PartyApplyRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.request.PartyApplyDto;
import com.example.villagerservice.party.request.PartyApplyDto.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@ExtendWith(MockitoExtension.class)
class PartyApplyServiceImplTest {
    @Mock
    private PartyRepository partyRepository;
    @Mock
    private PartyApplyRepository partyApplyRepository;
    @InjectMocks
    private PartyApplyServiceImpl partyApplyService;

    @Test
    @DisplayName("모임 신청 성공")
    void partyApplyTestSuccess() {
        // given
        String email = "test@naver.com";
        Long partyId = 1L;
        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.of(Party.builder()
                    .id(partyId)
                    .build()));
        given(partyApplyRepository.existsByParty_Member_EmailAndParty_Id(anyString(), anyLong()))
            .willReturn(false);
        given(partyApplyRepository.save(any()))
            .willReturn(PartyApply.builder()
                .id(1L)
                .targetMemberId(1L)
                .isAccept(false)
                .party(Party.builder().id(partyId).build())
                .build());
        ArgumentCaptor<PartyApply> captor = ArgumentCaptor.forClass(PartyApply.class);
        // when
        PartyApplyDto.Response response = partyApplyService.applyParty(email, partyId);

        // then
        verify(partyApplyRepository, times(1)).save(captor.capture());
        assertEquals(partyId, captor.getValue().getParty().getId());
        assertEquals(false, response.isAccept());
        assertEquals(1L, response.getPartyId());
    }

    @Test
    @DisplayName("모임 신청 실패 - 이미 신청한 모임")
    void partyApplyTestFailedPartyNotFound() {
        // given
        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.of(Party.builder().build()));

        // when
        PartyApplyException exception = assertThrows(PartyApplyException.class,
            () -> partyApplyService.applyParty("123", 1L));

        // then
        assertEquals(PartyApplyErrorCode.ALREADY_BEAN_APPLIED.getErrorCode(), exception.getErrorCode());
    }

    @Test
    @DisplayName("모임 신청 실패 - 이미 신청한 모임")
    void partyApplyTestFailedAlreadyApplied() {
        // when
        PartyApplyException exception = assertThrows(PartyApplyException.class,
            () -> partyApplyService.applyParty("123", -1L));

        // then
        assertEquals(PartyApplyErrorCode.PARTY_NOT_FOUND.getErrorCode(), exception.getErrorCode());
    }

    @Test
    @DisplayName("모임 신청 목록 조회 성공")
    void getApplyPartyListSuccess() {
        // given
        List<PartyApply> list = new ArrayList<>();
        Long partyId = 1L;
        Long targetMemberId = 1L;
        list.add(PartyApply.builder()
            .id(1L)
            .targetMemberId(targetMemberId)
            .isAccept(false)
            .party(Party.builder().id(partyId).build())
            .build());
        list.add(PartyApply.builder()
            .id(2L)
            .targetMemberId(targetMemberId)
            .isAccept(false)
            .party(Party.builder().id(partyId).build())
            .build());
        Page<PartyApply> pageList = new PageImpl<>(list);

        given(partyRepository.existsById(anyLong()))
            .willReturn(true);
        given(partyApplyRepository.findByParty_Id(anyLong(), any()))
            .willReturn(pageList);

        // when
        Page<PartyApplyDto.Response> response = partyApplyService.getApplyPartyList(1L, null);

        // then
        assertEquals(list.size(), response.getTotalElements());
    }

    @Test
    @DisplayName("모임 신청 목록 조회 실패 - 해당 모임이 없는 경우")
    void getApplyPartyListFailedEmptyParty() {
        // given
        given(partyRepository.existsById(anyLong()))
            .willReturn(false);

        // when
        PartyApplyException exception = assertThrows(PartyApplyException.class,
            () -> partyApplyService.getApplyPartyList(1L, null));

        // then
        assertEquals(PartyApplyErrorCode.PARTY_NOT_FOUND.getErrorCode(), exception.getErrorCode());
    }
}