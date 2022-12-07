package com.example.villagerservice.party.service;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.exception.PartyApplyErrorCode;
import com.example.villagerservice.party.exception.PartyApplyException;
import com.example.villagerservice.party.exception.PartyErrorCode;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.repository.PartyQueryRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.service.impl.PartyQueryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PartyQueryServiceImplTest {

    @Mock
    PartyQueryRepository partyQueryRepository;
    @InjectMocks
    PartyQueryServiceImpl partyQueryService;
    
    @Test
    @DisplayName("모임 조회 시 , 모임이 없을 경우")
    public void getPartyWithoutParty() {

        PartyException partyException = assertThrows(PartyException.class, () -> {
            partyQueryService.getParty(1L);
        });

        assertEquals(partyException.getErrorCode(), PartyErrorCode.PARTY_NOT_FOUND.getErrorCode());

    }

    @Test
    @DisplayName("모임 조회 테스트")
    public void getParty() {

        Long partyId = 1L;

        PartyDTO.Request partyRequest = PartyDTO.Request.builder()
                .partyName("test-party")
                .score(100)
                .startDt(LocalDateTime.now())
                .endDt(LocalDateTime.now().plusHours(2))
                .amount(1000)
                .build();

        Member member = Member.builder()
                .nickname("홍길동")
                .encodedPassword("1234")
                .email("test@gmail.com")
                .build();

        Party party = Party.createParty(
                partyRequest.getPartyName(),
                partyRequest.getScore(),
                partyRequest.getStartDt(),
                partyRequest.getEndDt(),
                partyRequest.getAmount(),
                member
        );

        PartyDTO.Response partyResponse = PartyDTO.Response.createPartyResponse(party);

        given(partyQueryRepository.getParty(anyLong())).willReturn(Optional.of(partyResponse));

        PartyDTO.Response result = partyQueryService.getParty(partyId);

        Assertions.assertEquals("test-party" , result.getPartyName());
        Assertions.assertEquals("홍길동" , result.getNickname());

    }
}
