
package com.example.villagerservice.party.service.impl;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyTag;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.dto.PartyListDTO;
import com.example.villagerservice.party.repository.PartyQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PartyQueryServiceImplTest {

    @Mock
    PartyQueryRepository partyQueryRepository;
    @InjectMocks
    PartyQueryServiceImpl partyQueryService;

    @Test
    @DisplayName("모임 전체 조회 테스트")
    void getAllParty(){

        PartyDTO.Request request = createRequest();

        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("홍길동")
                .build();

        Party party = Party.createParty(request , member);

        List<Party> list = new ArrayList<>();

        list.add(Party.createParty(request , member));
        list.add(Party.createParty(request , member));

        PartyListDTO partyListDTO = PartyListDTO.builder()
                .partyName(request.getPartyName())
                .nickname(member.getMemberDetail().getNickname())
                .build();

        List<PartyListDTO> responseList = new ArrayList<>();
        responseList.add(PartyListDTO.builder()
                .partyName(request.getPartyName())
                .nickname(member.getMemberDetail().getNickname())
                .build());
        responseList.add(PartyListDTO.builder()
                .partyName(request.getPartyName())
                .nickname(member.getMemberDetail().getNickname())
                .build());

        given(partyQueryRepository.getPartyList(anyString() , anyDouble() , anyDouble()))
                .willReturn(responseList);

        List<PartyListDTO> partyList = partyQueryService.getPartyList(member.getEmail(), 127.1, 127.1);
        org.assertj.core.api.Assertions.assertThat(partyList.size()).isEqualTo(2);
    }

    private static PartyDTO.Request createRequest() {
        List<PartyTag> tagList = new ArrayList<>();

        tagList.add(PartyTag.builder()
                .tagName("낚시")
                .build());
        tagList.add(PartyTag.builder()
                .tagName("볼링")
                .build());

        PartyDTO.Request request = PartyDTO.Request.builder()
                .partyName("test-party")
                .score(100)
                .startDt(LocalDate.now())
                .endDt(LocalDate.now().plusDays(2))
                .amount(1000)
                .numberPeople(2)
                .location("수원시")
                .latitude(127.1)
                .longitude(127.1)
                .content("test")
                .tagList(tagList)
                .build();

        return request;
    }
}
