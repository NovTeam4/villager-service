package com.example.villagerservice.party.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.exception.PartyErrorCode;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.repository.PartyRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


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
}
