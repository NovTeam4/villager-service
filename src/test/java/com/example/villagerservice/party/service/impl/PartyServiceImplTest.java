package com.example.villagerservice.party.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.villagerservice.events.service.impl.PartyCreatedEventServiceImpl;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyApply;
import com.example.villagerservice.party.domain.PartyMember;
import com.example.villagerservice.party.domain.PartyTag;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.dto.PartyListDTO;
import com.example.villagerservice.party.dto.UpdatePartyDTO;
import com.example.villagerservice.party.exception.PartyErrorCode;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.repository.PartyApplyRepository;
import com.example.villagerservice.party.repository.PartyMemberRepository;
import com.example.villagerservice.party.repository.PartyQueryRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.repository.PartyTagRepository;
import com.example.villagerservice.party.service.PartyApplyQueryService;
import com.example.villagerservice.party.service.PartyCommentService;
import com.example.villagerservice.party.service.PartyLikeService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
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

    @Mock
    PartyApplyQueryService partyApplyQueryService;

    @Mock
    PartyApplyRepository partyApplyRepository;

    @Mock
    PartyMemberRepository partyMemberRepository;

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
    @Disabled
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


        // then
        List<PartyListDTO> partyList = partyQueryService.getPartyList(member.getEmail(), 127.1, 127.1);

        // then
        Assertions.assertThat(partyList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("모임 시작 실패 - 모임이 없음")
    public void 모임_시작_실패_모임이없음(){
        // given
        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.empty());

        // when
        PartyException partyException = assertThrows(PartyException.class, () -> {
            partyService.startParty(1L, Member.builder().build());
        });

        // then
        assertThat(partyException.getErrorCode()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND.getErrorCode());
        assertThat(partyException.getErrorMessage()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("모임 시작 실패 - 자신이 모임장이 아님")
    public void 모임_시작_실패_자신이모임장이아님(){
        // given
        Member host = Member.builder().email("host@naver.com").build();// 모임장
        Member user = Member.builder().email("user@naver.com").build();// 로그인한 사용자

        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.ofNullable(Party.builder().member(host).build()));

        // when
        PartyException partyException = assertThrows(PartyException.class, () -> {
            partyService.startParty(1L, user);
        });

        // then
        assertThat(partyException.getErrorCode()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND_MEMBER.getErrorCode());
        assertThat(partyException.getErrorMessage()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND_MEMBER.getErrorMessage());
    }

    @Test
    @DisplayName("모임 시작 실패 - 시작시간이 안넘었음")
    public void 모임_시작_실패_시작시간이안넘었음(){
        // given
        LocalDate startDt = LocalDate.MAX;// 시작시간
        Member member = Member.builder().email("member@naver.com").build();// 모임장

        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.ofNullable(Party.builder().member(member).startDt(startDt).build()));

        // when
        PartyException partyException = assertThrows(PartyException.class, () -> {
            partyService.startParty(1L, member);
        });

        // then
        assertThat(partyException.getErrorCode()).isEqualTo(PartyErrorCode.PARTY_NOT_START_TIME.getErrorCode());
        assertThat(partyException.getErrorMessage()).isEqualTo(PartyErrorCode.PARTY_NOT_START_TIME.getErrorMessage());
    }

    @Test
    @DisplayName("모임 시작 실패 - 허락된 멤버가 한명도 없음")
    public void 모임_시작_실패_허락된멤버가한명도없음(){
        // given
        LocalDate startDt = LocalDate.now();// 시작시간
        Member member = Member.builder().email("member@naver.com").build();// 모임장
        Party party = Party.builder().member(member).startDt(startDt).build();
        List<PartyApply> partyApplyList = new ArrayList<>();
        for(long i = 1; i <= 10; i++){
            partyApplyList.add(PartyApply.createPartyList(party, i));
        }

        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.of(party));
        given(partyApplyQueryService.getPartyApplyId(anyLong(), anyString()))
            .willReturn(partyApplyList);// 10개 다 허락안한 신청

        // when
        PartyException partyException = assertThrows(PartyException.class, () -> {
            partyService.startParty(1L, member);
        });

        // then
        assertThat(partyException.getErrorCode()).isEqualTo(PartyErrorCode.PARTY_MEMBER_EMPTY.getErrorCode());
        assertThat(partyException.getErrorMessage()).isEqualTo(PartyErrorCode.PARTY_MEMBER_EMPTY.getErrorMessage());
    }

    @Test
    @DisplayName("모임 시작 성공")
    public void 모임_시작_성공(){
        // given
        LocalDate startDt = LocalDate.now();// 시작시간
        Member member = Member.builder().email("member@naver.com").build();// 모임장
        Party party = Party.builder().member(member).startDt(startDt).build();
        List<PartyApply> partyApplyList = new ArrayList<>();
        for(long i = 1; i <= 10; i++){
            partyApplyList.add(PartyApply.builder()
                    .targetMemberId(i)
                    .isAccept(true)
                    .party(party)
                    .build());
        }

        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.of(party));
        given(partyApplyQueryService.getPartyApplyId(anyLong(), anyString()))
            .willReturn(partyApplyList);// 10개 다 허락안한 신청

        // when
        partyService.startParty(1L, member);

        // then
    }

    @Test
    @DisplayName("모임 연장 실패 - 모임이 없음")
    public void 모임_연장_실패_모임이없음(){
        // given
        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.empty());

        // when
        PartyException partyException = assertThrows(PartyException.class, () -> {
            partyService.extensionParty(1L, null, null);
        });

        // then
        assertThat(partyException.getErrorCode()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND.getErrorCode());
        assertThat(partyException.getErrorMessage()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("모임 연장 실패 - 자신이 모임장이 아님")
    public void 모임_연장_실패_자신이모임장이아님(){
        // given
        String hostEmail = "host@naver.com";
        String loginEmail = "login@naver.com";
        // hostEmail로 되어있는모임
        Party party = Party.builder().member(Member.builder().email(hostEmail).build()).build();
        // 로그인한 멤버
        Member user = Member.builder().email(loginEmail).build();

        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.of(party));

        // when
        PartyException partyException = assertThrows(PartyException.class, () -> {
            partyService.extensionParty(1L, user, null);
        });

        // then
        assertThat(partyException.getErrorCode()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND_MEMBER.getErrorCode());
        assertThat(partyException.getErrorMessage()).isEqualTo(PartyErrorCode.PARTY_NOT_FOUND_MEMBER.getErrorMessage());
    }

    @Test
    @DisplayName("모임 연장 실패 - 종료시간이 원래 종료시간보다 이전")
    public void 모임_연장_실패_종료시간이원래종료시간보다이전(){
        // given
        LocalDate endDt = LocalDate.parse("2022-12-12");// 모임 기존 종료날
        LocalDate newEndDt = LocalDate.parse("2022-11-11");// 새로 등록할 종료날
        Member member = Member.builder().email("user@naver.com").build();
        Party party = Party.builder().endDt(endDt).member(member).build();

        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.of(party));

        // when
        PartyException partyException = assertThrows(PartyException.class, () -> {
            partyService.extensionParty(1L, member, newEndDt);
        });

        // then
        assertThat(partyException.getErrorCode()).isEqualTo(PartyErrorCode.PARTY_WRONG_END_TIME.getErrorCode());
        assertThat(partyException.getErrorMessage()).isEqualTo(PartyErrorCode.PARTY_WRONG_END_TIME.getErrorMessage());
    }

    @Test
    @DisplayName("모임 연장 실패 - 모임이 시작되지 않음")
    public void 모임_연장_실패_모임이시작되지않음(){
        // given
        LocalDate endDt = LocalDate.parse("2022-12-12");// 모임 기존 종료날
        LocalDate newEndDt = LocalDate.parse("2022-12-13");// 새로 등록할 종료날
        Member member = Member.builder().email("user@naver.com").build();
        Party party = Party.builder().endDt(endDt).member(member).build();

        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.of(party));
        given(partyMemberRepository.existsByParty_Id(anyLong()))
            .willReturn(false);

        // when
        PartyException partyException = assertThrows(PartyException.class, () -> {
            partyService.extensionParty(1L, member, newEndDt);
        });

        // then
        assertThat(partyException.getErrorCode()).isEqualTo(PartyErrorCode.PARTY_DOES_NOT_START.getErrorCode());
        assertThat(partyException.getErrorMessage()).isEqualTo(PartyErrorCode.PARTY_DOES_NOT_START.getErrorMessage());
    }

    @Test
    @DisplayName("모임 연장 실패 - 아직 종료시간이 아님")
    public void 모임_연장_실패_아직종료시간이아님(){
        // given
        LocalDate endDt = LocalDate.now().plusDays(1);// 모임 기존 종료날(now + 1)
        LocalDate newEndDt = LocalDate.now().plusDays(2);// 새로 등록할 종료날(now + 2)
        Member member = Member.builder().email("user@naver.com").build();
        Party party = Party.builder().endDt(endDt).member(member).build();

        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.of(party));
        given(partyMemberRepository.existsByParty_Id(anyLong()))
            .willReturn(true);

        // when
        PartyException partyException = assertThrows(PartyException.class, () -> {
            partyService.extensionParty(1L, member, newEndDt);
        });

        // then
        assertThat(partyException.getErrorCode()).isEqualTo(PartyErrorCode.PARTY_NOT_END_TIME.getErrorCode());
        assertThat(partyException.getErrorMessage()).isEqualTo(PartyErrorCode.PARTY_NOT_END_TIME.getErrorMessage());
    }

    @Test
    @DisplayName("모임 연장 성공")
    public void 모임_연장_성공(){
        // given
        LocalDate endDt = LocalDate.now().minusDays(1);// 모임 기존 종료날(now - 1)
        LocalDate newEndDt = LocalDate.now().plusDays(1);// 새로 등록할 종료날(now + 1)
        Member member = Member.builder().email("user@naver.com").build();
        Party party = Party.builder().endDt(endDt).member(member).build();

        given(partyRepository.findById(anyLong()))
            .willReturn(Optional.of(party));
        given(partyMemberRepository.existsByParty_Id(anyLong()))
            .willReturn(true);
        ArgumentCaptor<Party> captor = ArgumentCaptor.forClass(Party.class);

        // when
        partyService.extensionParty(1L, member, newEndDt);

        // then
        verify(partyRepository, times(1)).save(captor.capture());
        assertThat(newEndDt).isEqualTo(captor.getValue().getEndDt());
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
