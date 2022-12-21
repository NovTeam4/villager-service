package com.example.villagerservice.party.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyApply;
import com.example.villagerservice.party.exception.PartyApplyErrorCode;
import com.example.villagerservice.party.exception.PartyApplyException;
import com.example.villagerservice.party.repository.PartyApplyRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.dto.PartyApplyDto;
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
        Long memberId = 1L;
        Long partyId = 1L;
        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.of(Party.builder()
                    .id(partyId)
                    .build()));
        given(partyApplyRepository.existsByTargetMemberIdAndParty_Id(anyLong(), anyLong()))
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
        PartyApplyDto.Response response = partyApplyService.applyParty(memberId, partyId);

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
        given(partyApplyRepository.existsByTargetMemberIdAndParty_Id(anyLong(), any()))
            .willReturn(true);

        // when
        PartyApplyException exception = assertThrows(PartyApplyException.class,
            () -> partyApplyService.applyParty(1L, 1L));

        // then
        assertEquals(PartyApplyErrorCode.ALREADY_BEAN_APPLIED.getErrorCode(), exception.getErrorCode());
    }

    @Test
    @DisplayName("모임 신청 실패 - 이미 신청한 모임")
    void partyApplyTestFailedAlreadyApplied() {
        // when
        PartyApplyException exception = assertThrows(PartyApplyException.class,
            () -> partyApplyService.applyParty(1L, -1L));

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

    @Test
    @DisplayName("모임 허락 성공")
    void 모임_허락_성공() {
        // given
        Long partyId = 1L;
        Long targetMemberId = 2L;
        String email = "host@123";
        Party party = Party.builder()
            .member(Member.builder().email(email).build())
            .build();

        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.ofNullable(party));
        given(partyApplyRepository.findByParty_IdAndTargetMemberId(anyLong(), anyLong()))
            .willReturn(Optional.ofNullable(PartyApply.builder()
                .id(1L)
                .party(party)
                .targetMemberId(targetMemberId)
                .isAccept(false)
                .build()));
        given(partyApplyRepository.save(any()))
            .willReturn(PartyApply.builder()
                .id(1L)
                .party(party)
                .targetMemberId(targetMemberId)
                .isAccept(true)
                .build());

        ArgumentCaptor<PartyApply> captor = ArgumentCaptor.forClass(PartyApply.class);

        // when
        PartyApplyDto.Response response
            = partyApplyService.partyPermission(partyId, targetMemberId, email);

        // then
        verify(partyApplyRepository, times(1)).save(captor.capture());
        assertEquals(true, captor.getValue().isAccept());
        assertEquals(true, response.isAccept());
    }

    @Test
    @DisplayName("모임 허락 취소 성공")
    void 모임_허락_취소성공() {
        // given
        Long partyId = 1L;
        Long targetMemberId = 2L;
        String email = "host@123";
        Party party = Party.builder()
            .member(Member.builder().email(email).build())
            .build();

        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.ofNullable(party));
        given(partyApplyRepository.findByParty_IdAndTargetMemberId(anyLong(), anyLong()))
            .willReturn(Optional.ofNullable(PartyApply.builder()
                .id(1L)
                .party(party)
                .targetMemberId(targetMemberId)
                .isAccept(true)
                .build()));
        given(partyApplyRepository.save(any()))
            .willReturn(PartyApply.builder()
                .id(1L)
                .party(party)
                .targetMemberId(targetMemberId)
                .isAccept(false)
                .build());

        ArgumentCaptor<PartyApply> captor = ArgumentCaptor.forClass(PartyApply.class);

        // when
        PartyApplyDto.Response response
            = partyApplyService.partyPermission(partyId, targetMemberId, email);

        // then
        verify(partyApplyRepository, times(1)).save(captor.capture());
        assertEquals(false, captor.getValue().isAccept());
    }

    @Test
    @DisplayName("모임 허락 실패 - 해당 모임이 없는 경우")
    void 모임_허락_실패_해당모임이없는경우() {
        // given
        // when
        PartyApplyException exception = assertThrows(PartyApplyException.class,
            () -> partyApplyService.partyPermission(-1L, 1L, "host@123"));

        // then
        assertEquals(PartyApplyErrorCode.PARTY_NOT_FOUND.getErrorCode(), exception.getErrorCode());
    }

    @Test
    @DisplayName("모임 허락 실패 - 가져온 모임이 주최자의 이메일과 다름")
    void 모임_허락_실패_가져온모임이주최자의이메일과다름() {
        String emailHost = "host@123";
        String emailFake = "fake@123";
        // given
        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.ofNullable(Party.builder()
                .member(Member.builder().email(emailFake).build())
                .build()));

        // when
        PartyApplyException exception = assertThrows(PartyApplyException.class,
            () -> partyApplyService.partyPermission(1L, 2L, emailHost));

        // then
        assertEquals(PartyApplyErrorCode.DIFFERENT_HOST.getErrorCode(), exception.getErrorCode());
    }

    @Test
    @DisplayName("모임 허락 실패 - 등록된 신청이 없음")
    void 모임_허락_실패_등록된신청이없음() {
        // given
        String email = "host@123";
        Party party = Party.builder()
            .member(Member.builder().email(email).build())
            .build();

        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.ofNullable(party));

        // when
        PartyApplyException exception = assertThrows(PartyApplyException.class,
            () -> partyApplyService.partyPermission(1L, -1L, email));

        // then
        assertEquals(PartyApplyErrorCode.PARTY_APPLY_NOT_FOUND.getErrorCode(), exception.getErrorCode());
    }
}