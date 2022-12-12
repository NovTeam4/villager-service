package com.example.villagerservice.party.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.dto.UpdatePartyDTO;
import com.example.villagerservice.party.exception.PartyErrorCode;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.repository.PartyRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


@ExtendWith(MockitoExtension.class)
public class PartyServiceImplTest {

    @Mock
    PartyRepository partyRepository;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    PartyServiceImpl partyService;


    @Test
    @DisplayName("모임 등록 시 , 회원 없을 경우")
    public void createPartyWithoutMember() {

        PartyDTO.Request partyRequest = PartyDTO.Request.builder()
                .partyName("test-party")
                .score(100)
                .startDt(LocalDateTime.now())
                .endDt(LocalDateTime.now())
                .amount(1000)
                .build();

        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("홍길동")
                .build();


        PartyException partyException = assertThrows(PartyException.class, () -> {
            partyService.createParty(member.getId(), partyRequest);
        });

        assertThat(partyException.getErrorCode()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND_MEMBER.getErrorCode());


    }

    @Test
    @DisplayName("모임 등록 테스트")
    public void createParty(){

        PartyDTO.Request partyRequest = PartyDTO.Request.builder()
                .partyName("test-party")
                .score(100)
                .startDt(LocalDateTime.now())
                .endDt(LocalDateTime.now())
                .amount(1000)
                .build();


        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder()
                        .nickname("홍길동")
                        .encodedPassword("1234")
                        .email("test@gmail.com")
                        .build()));

        ArgumentCaptor<Party> captor = ArgumentCaptor.forClass(Party.class);

        partyService.createParty(1L , partyRequest);

        verify(partyRepository,times(1)).save(captor.capture());
        assertEquals("test-party" , captor.getValue().getPartyName());
        assertEquals(100, captor.getValue().getScore());
        assertEquals("홍길동" , captor.getValue().getMember().getMemberDetail().getNickname());

    }

    @Test
    @DisplayName("모임 삭제 시, 모임이 없을 경우")
    void deletePartyWithoutParty() {

        PartyException partyException = assertThrows(PartyException.class, () -> {
            partyService.deleteParty(anyLong());
        });

        assertThat(partyException.getErrorCode()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND.getErrorCode());
        assertThat(partyException.getErrorMessage()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND.getErrorMessage());

    }

    @Test
    @DisplayName("모임 삭제 테스트")
    void deleteParty() {

        PartyDTO.Request partyRequest = PartyDTO.Request.builder()
                .partyName("test-party")
                .score(100)
                .startDt(LocalDateTime.now())
                .endDt(LocalDateTime.now())
                .amount(1000)
                .build();

        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("홍길동")
                .build();

        Party party = Party.createParty(
                partyRequest.getPartyName(),
                partyRequest.getScore(),
                partyRequest.getStartDt(),
                partyRequest.getEndDt(),
                partyRequest.getAmount(),
                member
        );

        given(partyRepository.findById(1L)).willReturn(Optional.of(party));

        partyService.deleteParty(1L);

        List<Party> parties = partyRepository.findAll();

        assertEquals(parties.size(),0);
    }

    @Test
    @DisplayName("모임 변경 시, 모임이 없을 경우")
    void updatePartyWithoutParty() {

        Long partyId = 1L;
        PartyException partyException = assertThrows(PartyException.class, () -> {
            partyService.updateParty(partyId , any());
        });

        assertThat(partyException.getErrorCode()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND.getErrorCode());
        assertThat(partyException.getErrorMessage()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND.getErrorMessage());

    }

    @Test
    @DisplayName("모임 변경 테스트")
    void updateParty() {

        PartyDTO.Request partyRequest = PartyDTO.Request.builder()
                .partyName("test-party")
                .score(100)
                .startDt(LocalDateTime.now())
                .endDt(LocalDateTime.now())
                .amount(1000)
                .build();

        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("홍길동")
                .build();

        Party party = Party.createParty(
                partyRequest.getPartyName(),
                partyRequest.getScore(),
                partyRequest.getStartDt(),
                partyRequest.getEndDt(),
                partyRequest.getAmount(),
                member
        );

        UpdatePartyDTO.Request updateRequest = UpdatePartyDTO.Request.builder()
                .partyName("updateParty")
                .build();

        given(partyRepository.findById(1L)).willReturn(Optional.of(party));

        PartyDTO.Response response = partyService.updateParty(1L, updateRequest);

        assertThat(response.getPartyName()).isEqualTo("updateParty");

    }

    @Test
    @DisplayName("모임 전체 조회 시 , 모임이 없을 경우")
    void getAllPartyWithoutParty(){

        PartyException partyException = assertThrows(PartyException.class,
                () -> partyService.getAllParty(null));

        Assertions.assertThat(partyException.getErrorCode()).isEqualTo(PartyErrorCode.PARTY_NOT_REGISTERED.getErrorCode());
        Assertions.assertThat(partyException.getErrorMessage()).isEqualTo(PartyErrorCode.PARTY_NOT_REGISTERED.getErrorMessage());

    }

    @Test
    @DisplayName("모임 전체 조회 테스트")
    void getAllParty(){

        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("홍길동")
                .build();

        List<Party> list = new ArrayList<>();

        list.add(Party.createParty(
                "test-party",
                100,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                1000,
                member
        ));
        list.add(Party.createParty(
                "test-party2",
                100,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                1000,
                member
        ));

        Page<Party> partyPage = new PageImpl<>(list);

        given(partyRepository.count())
                .willReturn(2L);
        given(partyRepository.findAll((Pageable) any()))
                .willReturn(partyPage);

        Page<PartyDTO.Response> parties = partyService.getAllParty(null);
        Assertions.assertThat(parties.getSize()).isEqualTo(2);
        Assertions.assertThat(parties.getTotalElements()).isEqualTo(list.size());

    }
}
