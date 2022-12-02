package com.example.villagerservice.member.service.impl;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.exception.PartyErrorCode;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.request.PartyCreate;
import com.example.villagerservice.party.service.PartyServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.*;


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


        PartyException partyException = Assertions.assertThrows(PartyException.class, () -> {
            partyService.createParty(member.getEmail(), partyCreate);
        });

        assertThat(partyException.getErrorCode()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND_MEMBER);


    }

    @Test
    @DisplayName("모임 등록 테스트")
    public void createParty(){


    }
}
