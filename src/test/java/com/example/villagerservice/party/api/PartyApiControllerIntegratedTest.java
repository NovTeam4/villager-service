package com.example.villagerservice.party.api;

import static io.restassured.RestAssured.given;

import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.dto.LoginMember;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.repository.PartyQueryRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
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

    private RestDocumentationTemplate template = new RestDocumentationTemplate("Party api");

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void clean() {
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
                .startDt(LocalDateTime.now())
                .endDt(LocalDateTime.now().plusHours(2))
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
        Party party = saveParty();

        givenAuth("",
                template.requestRestDocumentation("모임 조회"))
                .when()
                .header(HttpHeaders.AUTHORIZATION , "Bearer " + jwtTokenResponse.getAccessToken())
                .get("/api/v1/parties/" + party.getId())
                .then()
                .statusCode(HttpStatus.OK.value());

    }

    @Test
    @DisplayName("모임 삭제 테스트")
    void deleteParty() throws Exception {

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        Party party = saveParty();

        givenAuth("",
                template.requestRestDocumentation("모임 삭제"))
                .when()
                .header(HttpHeaders.AUTHORIZATION , "Bearer " + jwtTokenResponse.getAccessToken())
                .delete("/api/v1/parties/" + party.getId())
                .then()
                .statusCode(HttpStatus.OK.value());


        List<Party> parties = partyRepository.findAll();
        Assertions.assertThat(parties.size()).isEqualTo(0);

    }

    private JwtTokenResponse getJwtTokenResponse() throws JsonProcessingException {
        createMember();
        LoginMember.Request login = LoginMember.Request.builder()
                .email("test@gmail.com")
                .password("hello11@@nW")
                .build();

        Response response = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Content-type", "application/json")
                .body(objectMapper.writeValueAsString(login))
                .log().all()
                .post("/api/v1/auth/login");


        return objectMapper.readValue(response.asString(), JwtTokenResponse.class);
    }

    private void createMember() {
        Member member = Member.builder()
                .email("test@gmail.com")
                .encodedPassword(passwordEncoder.encode("hello11@@nW"))
                .nickname("original")
                .build();
        memberRepository.save(member);
    }

    private Party saveParty() {

        Party party = Party.createParty(
                "test-party",
                100,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                1000,
                null
        );

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
