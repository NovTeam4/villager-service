package com.example.villagerservice.party.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.example.villagerservice.events.service.impl.PartyCreatedEventServiceImpl;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyTag;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.dto.PartyListDTO;
import com.example.villagerservice.party.dto.UpdatePartyDTO;
import com.example.villagerservice.party.exception.PartyErrorCode;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.repository.PartyQueryRepository;
import com.example.villagerservice.party.repository.PartyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.villagerservice.party.repository.PartyTagRepository;
import com.example.villagerservice.party.service.PartyCommentService;
import com.example.villagerservice.party.service.PartyLikeService;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import javax.validation.constraints.NotNull;


@ExtendWith(MockitoExtension.class)
public class PartyServiceImplTest {

    @Mock
    PartyRepository partyRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PartyQueryRepository partyQueryRepository;

    @Mock
    PartyTagRepository partyTagRepository;

    @Mock
    PartyCreatedEventServiceImpl partyCreatedEventService;

    @Mock
    PartyCommentService partyCommentService;

    @Mock
    PartyLikeService partyLikeService;

    @InjectMocks
    PartyServiceImpl partyService;

    @InjectMocks
    PartyQueryServiceImpl partyQueryService;


    @Test
    @DisplayName("모임 등록 시 , 회원 없을 경우")
    public void createPartyWithoutMember() {

        PartyDTO.Request request = createRequest();

        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("홍길동")
                .build();


        PartyException partyException = assertThrows(PartyException.class, () -> {
            partyService.createParty(member.getId(), request);
        });

        assertThat(partyException.getErrorCode()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND_MEMBER.getErrorCode());


    }

    @Test
    @DisplayName("모임 등록 테스트")
    public void createParty(){

        PartyDTO.Request request = createRequest();


        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder()
                        .nickname("홍길동")
                        .encodedPassword("1234")
                        .email("test@gmail.com")
                        .build()));

        ArgumentCaptor<Party> captor = ArgumentCaptor.forClass(Party.class);

        partyService.createParty(1L , request);

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

        Party party = getParty();

        given(partyRepository.findById(1L)).willReturn(Optional.of(party));

        partyService.deleteParty(1L);

        List<Party> parties = partyRepository.findAll();

        assertEquals(parties.size(),0);
    }



    @Test
    @DisplayName("모임 변경 시, 모임이 없을 경우")
    void updatePartyWithoutParty() {

        UpdatePartyDTO.Request request = UpdatePartyDTO.Request.builder()
                .partyName("update-test-party")
                .build();
        Long partyId = 1L;
        PartyException partyException = assertThrows(PartyException.class, () -> {
            partyService.updateParty(partyId , request , "test@gmail.com");
        });

        assertThat(partyException.getErrorCode()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND.getErrorCode());
        assertThat(partyException.getErrorMessage()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND.getErrorMessage());

    }

    @Test
    @DisplayName("모임 변경 테스트")
    void updateParty() {

        Party party = getParty();

        UpdatePartyDTO.Request updateRequest = UpdatePartyDTO.Request.builder()
                .partyName("updateParty")
                .build();

        given(partyRepository.findById(1L)).willReturn(Optional.of(party));

        PartyDTO.Response response = partyService.updateParty(1L, updateRequest , anyString());

        assertThat(response.getPartyName()).isEqualTo("updateParty");

    }

    @Test
    @DisplayName("모임 조회 시 , 모임이 없을 경우")
    public void getPartyWithoutParty() {

        PartyException partyException = assertThrows(PartyException.class, () -> {
            partyService.getParty(1L , anyString());
        });

        assertEquals(partyException.getErrorCode(), PartyErrorCode.PARTY_NOT_FOUND.getErrorCode());

    }

    @Test
    @DisplayName("모임 조회 테스트")
    public void findByParty_id() {

        Long partyId = 1L;

        PartyDTO.Request request = createRequest();

        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("홍길동")
                .build();

        Party party = Party.createParty(request , member);

        given(partyRepository.findById(any())).willReturn(Optional.of(party));

        PartyDTO.Response result = partyService.getParty(partyId , member.getEmail());

        Assertions.assertThat(result.getPartyName()).isEqualTo("test-party");
        Assertions.assertThat(result.getNickname()).isEqualTo("홍길동");

    }

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
        Assertions.assertThat(partyList.size()).isEqualTo(2);
    }

    @NotNull
    private static Party getParty() {
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
                .tagList(getTagList())
                .build();

        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("홍길동")
                .build();

        return Party.createParty(request, member);
    }

    private static List<PartyTag> getTagList() {
        List<PartyTag> list = new ArrayList<>();

        PartyTag tag = PartyTag.builder()
                .tagName("test-tag")
                .build();

        list.add(tag);

        return list;
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
