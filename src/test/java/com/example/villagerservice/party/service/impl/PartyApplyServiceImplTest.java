package com.example.villagerservice.party.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.party.domain.PartyApply;
import com.example.villagerservice.party.exception.PartyApplyErrorCode;
import com.example.villagerservice.party.exception.PartyApplyException;
import com.example.villagerservice.party.repository.PartyApplyRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.request.PartyApplyDto;
import com.example.villagerservice.party.request.PartyCreate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

@ExtendWith(MockitoExtension.class)
class PartyApplyServiceImplTest {

    @Mock
    private PartyApplyRepository partyApplyRepository;

    @Mock
    private PartyRepository partyRepository;

    @InjectMocks
    private PartyApplyServiceImpl partyApplyService;

    @Test
    @DisplayName("모임 신청 실패 - 이미 신청한 모임")
    void partyApplyTestFailedAlreadyApplied() {
        // given
        Long partyId = 1L;

        PartyCreate partyCreate = PartyCreate.builder()
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

        given(partyRepository.existsById(partyId))
            .willReturn(true);

        given(partyApplyRepository.findByParty_Id(partyId, any()))
            .willReturn((Page<PartyApply>) PartyApply.builder().build());

        ArgumentCaptor<PartyApply> captor = ArgumentCaptor.forClass(PartyApply.class);

        // when
        Page<PartyApplyDto.Response> partyApplyDto = partyApplyService.getApplyPartyList(1L, null);

        // then
        verify(partyApplyRepository, times(1)).save(captor.capture());
        assertEquals(1L, captor.getValue().getId());
    }

    @Test
    @DisplayName("모임 신청 목록 조회 실패 - 해당 모임이 없는 경우")
    void getApplyPartyListTest() {
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