package com.example.villagerservice.party.service.impl;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyList;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.exception.PartyErrorCode;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.exception.PartyListErrorCode;
import com.example.villagerservice.party.exception.PartyListException;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.request.PartyCreate;
import com.example.villagerservice.party.service.impl.PartyServiceImpl;
import org.junit.jupiter.api.Assertions;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


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

        PartyCreate partyCreate = PartyCreate.builder()
                .partyName("TestParty")
                .score(20)
                .amount(10000)
                .build();

        Member member = Member.builder()
                .email("hello@naver.com")
                .nickname("Test")
                .build();


        PartyException partyException = assertThrows(PartyException.class, () -> {
            partyService.createParty(member.getEmail(), partyCreate);
        });

        assertThat(partyException.getErrorCode()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND_MEMBER.getErrorCode());


    }

    @Test
    @DisplayName("모임 등록 테스트")
    public void createParty(){

        PartyCreate partyCreate = PartyCreate.builder()
                .partyName("TestParty")
                .score(100)
                .startDt(LocalDateTime.now())
                .endDt(LocalDateTime.now().plusHours(2))
                .amount(1000)
                .build();

        String email = "123@123";

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(Member.builder()
                        .nickname("홍길동")
                        .encodedPassword("1234")
                        .email("123@123")
                        .build()));

        ArgumentCaptor<Party> captor = ArgumentCaptor.forClass(Party.class);

        partyService.createParty(email , partyCreate);

        verify(partyRepository,times(1)).save(captor.capture());
        assertEquals("TestParty",captor.getValue().getPartyName());
        assertEquals(100,captor.getValue().getScore());
        assertEquals(1000,captor.getValue().getAmount());

    }

    @Test
    @DisplayName("모임 조회 시 , 모임이 없을 경우")
    public void getPartyWithoutParty() {

        PartyCreate partyCreate = PartyCreate.builder()
                .partyName("TestParty")
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

        PartyListException partyListException = assertThrows(PartyListException.class, () -> {
            partyService.getParty(1L);
        });

        assertEquals(partyListException.getErrorCode(), PartyListErrorCode.PARTY_NOT_FOUND.getErrorCode());

    }

    @Test
    @DisplayName("모임 조회 테스트")
    public void getParty() {

        PartyCreate partyCreate = PartyCreate.builder()
                .partyName("TestParty")
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
                        .partyCreate(partyCreate)
                        .member(member)
                        .build()));

        PartyDTO party = partyService.getParty(1L);

        assertEquals("TestParty",party.getPartyName());
        assertEquals(100,party.getScore());
        assertEquals(member,party.getMember());
        assertEquals("홍길동",party.getMember().getNickname());

    }
}
