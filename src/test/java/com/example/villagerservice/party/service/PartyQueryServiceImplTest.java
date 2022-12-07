package com.example.villagerservice.party.service;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.exception.PartyApplyErrorCode;
import com.example.villagerservice.party.exception.PartyApplyException;
import com.example.villagerservice.party.repository.PartyQueryRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.service.impl.PartyQueryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PartyQueryServiceImplTest {

    @Mock
    PartyQueryRepository partyQueryRepository;

    @Mock
    PartyRepository partyRepository;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    PartyQueryServiceImpl partyQueryService;
    
    @Test
    @DisplayName("모임 조회 시 , 모임이 없을 경우")
    public void getPartyWithoutParty() {

        PartyApplyException partyListException = assertThrows(PartyApplyException.class, () -> {
            partyQueryService.getParty(1L);
        });

        assertEquals(partyListException.getErrorCode(), PartyApplyErrorCode.PARTY_NOT_FOUND.getErrorCode());

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

    }
}
