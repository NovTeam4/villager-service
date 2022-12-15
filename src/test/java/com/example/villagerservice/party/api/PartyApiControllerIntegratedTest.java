package com.example.villagerservice.party.api;


import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyComment;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.dto.UpdatePartyDTO;
import com.example.villagerservice.party.repository.PartyCommentRepository;
import com.example.villagerservice.party.repository.PartyQueryRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.print.attribute.standard.Media;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;


@Import({AuthConfig.class})
public class PartyApiControllerIntegratedTest extends BaseDocumentation {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private PartyQueryRepository partyQueryRepository;

    private RestDocumentationTemplate template = new RestDocumentationTemplate("모임 API");

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PartyCommentRepository partyCommentRepository;

    @BeforeEach
    void clean() {
        partyRepository.deleteAll();
        memberRepository.deleteAll();
        partyCommentRepository.deleteAll();
    }

    @AfterEach
    void afterClean() {
        partyCommentRepository.deleteAll();
        partyRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("모임 등록 테스트")
    void createParty() throws Exception {

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        PartyDTO.Request request = PartyDTO.Request.builder()
                .partyName("test-party")
                .score(100)
                .startDt(LocalDate.now())
                .endDt(LocalDate.now().plusDays(2))
                .amount(1000)
                .build();

        String value = objectMapper.writeValueAsString(request);


        givenAuth(value,
                template.requestRestDocumentation(
                        "모임 등록",
                        getCreatePartyDtoRequestFields(),
                        PartyDTO.Request.class.getName()))
                .when()
                .header(HttpHeaders.AUTHORIZATION , "Bearer " + jwtTokenResponse.getAccessToken())
                .post("/api/v1/parties")
                .then()
                .statusCode(HttpStatus.OK.value());

        List<Party> parties = partyRepository.findAll();
        Assertions.assertThat(parties.size()).isEqualTo(1);
        Assertions.assertThat(parties.get(0).getPartyName()).isEqualTo("test-party");
    }

    @Test
    @DisplayName("모임 조회 테스트")
    void getParty() throws Exception {

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        Member member = createMember();
        Party party = saveParty(member);
        Long partyId = party.getId();

        givenAuth("",
                template.requestRestDocumentation("모임 조회"))
                .when()
                .header(HttpHeaders.AUTHORIZATION , "Bearer " + jwtTokenResponse.getAccessToken())
                .get("/api/v1/parties/{partyId}",partyId)
                .then()
                .statusCode(HttpStatus.OK.value());

    }

    @Test
    @DisplayName("모임 삭제 테스트")
    void deleteParty() throws Exception {

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        Member member = createMember();
        Party party = saveParty(member);
        Long partyId = party.getId();

        givenAuth("",
                template.requestRestDocumentation("모임 삭제"))
                .when()
                .header(HttpHeaders.AUTHORIZATION , "Bearer " + jwtTokenResponse.getAccessToken())
                .delete("/api/v1/parties/{partyId}",partyId)
                .then()
                .statusCode(HttpStatus.OK.value());


        List<Party> parties = partyRepository.findAll();
        Assertions.assertThat(parties.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("모임 변경 테스트")
    void updateParty() throws Exception {

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        Member member = createMember();
        Party party = saveParty(member);
        Long partyId = party.getId();

        UpdatePartyDTO.Request request = UpdatePartyDTO.Request.builder()
                .partyName("updateTest")
                .build();

        String value = objectMapper.writeValueAsString(request);

        System.out.println("partyId = " + partyId);

        givenAuth(value,
                template.requestRestDocumentation("모임 변경"))
                .when()
                .header(HttpHeaders.AUTHORIZATION , "Bearer " + jwtTokenResponse.getAccessToken())
                .patch("/api/v1/parties/{partId}" , partyId)
                .then()
                .statusCode(HttpStatus.OK.value());

        Party findParty = partyRepository.findById(partyId).get();
        Assertions.assertThat(findParty.getPartyName()).isEqualTo("updateTest");
    }

    @Test
    @DisplayName("모임 전체 조회 테스트")
    void getAllParty() throws Exception {

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        Member member = createMember();
        Party party = saveParty(member);
        Party party2 = saveParty(member);

        givenAuth("",
                template.requestRestDocumentation("모임 전체 조회"))
                .when()
                .header(HttpHeaders.AUTHORIZATION , "Bearer " + jwtTokenResponse.getAccessToken())
                .get("/api/v1/parties")
                .then()
                .statusCode(HttpStatus.OK.value());

        List<Party> parties = partyRepository.findAll();
        Assertions.assertThat(parties.size()).isEqualTo(2);
        Assertions.assertThat(parties.get(0).getPartyName()).isEqualTo(party.getPartyName());
    }

    @Test
    @DisplayName("모임 댓글 등록 테스트")
    void createPartyComment() throws Exception {

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        Member member = createMember();
        Party party = saveParty(member);

        String value = "test!";

        givenAuth(value,
                template.requestRestDocumentation("모임 댓글 작성"))
                .when()
                .header(HttpHeaders.AUTHORIZATION , "Bearer " + jwtTokenResponse.getAccessToken())
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .post("/api/v1/parties/{partyId}/comment" , party.getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        PartyComment partyComment = partyCommentRepository.findByParty_id(party.getId());
        Assertions.assertThat(partyComment.getContents()).isEqualTo("test!");
    }

    private Member createMember() {
        Member member = Member.builder()
                .email("testparty@gmail.com")
                .encodedPassword(passwordEncoder.encode("hello11@naver.com"))
                .nickname("홍길동")
                .build();
        memberRepository.save(member);

        return member;
    }

    private Party saveParty(Member member) {

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
                .tagList(null)
                .build();

        Party party = Party.createParty(request , member);

        partyRepository.save(party);

        return party;
    }

    private List<FieldDescriptor> getCreatePartyDtoRequestFields() {
        return List.of(
                fieldWithPath("partyName").type(JsonFieldType.STRING).description("모임이름"),
                fieldWithPath("score").type(JsonFieldType.NUMBER).description("모임 점수"),
                fieldWithPath("startDt").type(JsonFieldType.STRING).description("모임 시작 시간"),
                fieldWithPath("endDt").type(JsonFieldType.STRING).description("모임 종료 시간"),
                fieldWithPath("amount").type(JsonFieldType.NUMBER).description("모임 금액"));
    }


}
