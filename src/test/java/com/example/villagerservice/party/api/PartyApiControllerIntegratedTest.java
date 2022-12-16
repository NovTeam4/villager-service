package com.example.villagerservice.party.api;


import static com.example.villagerservice.party.type.PartyLikeResponseType.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyApply;
import com.example.villagerservice.party.domain.PartyLike;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.dto.UpdatePartyDTO;
import com.example.villagerservice.party.repository.PartyApplyRepository;
import com.example.villagerservice.party.repository.PartyLikeRepository;
import com.example.villagerservice.party.domain.PartyComment;
import com.example.villagerservice.party.repository.PartyCommentRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.request.PartyApplyDto;
import com.example.villagerservice.party.request.PartyLikeDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import java.util.Arrays;
import java.util.List;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
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

    @Autowired
    private PartyApplyRepository partyApplyRepository;

    @Autowired
    private PartyLikeRepository partyLikeRepository;

    @BeforeEach
    void clean() {
        partyApplyRepository.deleteAll();
        partyLikeRepository.deleteAll();
        partyRepository.deleteAll();
        memberRepository.deleteAll();
        partyCommentRepository.deleteAll();
    }

    @AfterEach
    void afterClean() {
        partyCommentRepository.deleteAll();
        partyApplyRepository.deleteAll();
        partyLikeRepository.deleteAll();
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
        Member member = createMember("testparty@gmail.com", "홍길동");
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
        Member member = createMember("testparty@gmail.com", "홍길동");
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
        Member member = createMember("testparty@gmail.com", "홍길동");
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
        Member member = createMember("testparty@gmail.com", "홍길동");
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
        Member member = createMember("testparty@gmail.com", "홍길동");
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

    private Member createMember(String email, String nickname) {
        Member member = Member.builder()
                .email(email)
                .encodedPassword(passwordEncoder.encode("hello11@@nW"))
                .nickname(nickname)
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

    @Test
    @DisplayName("모임 신청 테스트")
    void applyParty() throws Exception {
        Member host = createMember("host@gmail.com", "주최자");
        Member applicant = createMember("applicant@gmail.com", "신청자");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse(applicant.getEmail(), "hello11@@nW");// 신청자로 로그인
        Party party = saveParty(host);// 주최자기준

        givenAuth("",
            template.allRestDocumentation("모임 신청",
                getApplyPartyPathParameterFields(),
                getPartyApplyDtoResponseFields(),
                PartyApplyDto.Response.class.getName()))
            .when()
            .header(HttpHeaders.AUTHORIZATION , "Bearer " + jwtTokenResponse.getAccessToken())
            .post("/api/v1/parties/{partyId}/apply", party.getId())
            .then()
            .statusCode(HttpStatus.OK.value());

        PartyApply partyApply = partyApplyRepository.findFirstByOrderByIdDesc().get();
        Assertions.assertThat(partyApply.isAccept()).isEqualTo(false);
        Assertions.assertThat(partyApply.getParty().getId()).isEqualTo(party.getId());
    }

    @Test
    @DisplayName("모임 신청 조회 테스트")
    void getApplyPartyList() throws Exception {
        Member host = createMember("host@gmail.com", "주최자");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse(host.getEmail(), "hello11@@nW");// 신청자로 로그인
        Party party = saveParty(host);// 파티생성
        int page = 0;
        int size = 3;

        // 모임 신청 10개
        int applyCnt = 10;
        for(long i = 1; i <= applyCnt; i++){
            savePartyApply(i, party);
        }

        Response response = givenAuth("",
            template.responseRestDocumentation(
                "모임 신청 조회",
                getPartyApplyDtoListRequestParameterFields(),
                getApplyPartyPathParameterFields(),
                getPartyApplyDtoListResponseFields(),
                PartyApplyDto.Response.class.getName()))
            .when()
            .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
            .get("/api/v1/parties/{partyId}/apply?page={page}&size={size}", party.getId(), page, size);

        response
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("totalElements", Matchers.equalTo(applyCnt));
    }

    @Test
    @DisplayName("모임 허락 테스트")
    void partyPermission() throws Exception {
        Member host = createMember("host@gmail.com", "주최자");
        Member applicant = createMember("applicant@gmail.com", "신청자");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse(host.getEmail(), "hello11@@nW");// 신청자로 로그인
        Party party = saveParty(host);// 주최자기준
        savePartyApply(applicant.getId(), party);// 파티신청

        Response response = givenAuth("",
            template.allRestDocumentation("모임 허락",
                partyPermissionPathParameterFields(),
                getPartyApplyDtoResponseFields(),
                PartyApplyDto.Response.class.getName()))
            .when()
            .header(HttpHeaders.AUTHORIZATION , "Bearer " + jwtTokenResponse.getAccessToken())
            .patch("/api/v1/parties/{partyId}/permission/{targetMemberId}", party.getId(), applicant.getId());

        response
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("accept", Matchers.equalTo(true));
    }

    // 파티 신청
    private void savePartyApply(Long targetMemberId, Party party) {
        partyApplyRepository.save(PartyApply.builder()
                .party(party)
                .isAccept(false)
                .targetMemberId(targetMemberId)
                .build());
    }

    @Test
    @DisplayName("관심 모임 등록 테스트")
    void partyLike() throws Exception {
        Member member = createMember("host@gmail.com", "주최자");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse(member.getEmail(), "hello11@@nW");// 신청자로 로그인
        Party party = saveParty(member);// 주최자기준

        Response response = givenAuth("",
            template.allRestDocumentation("모임 좋아요",
                getApplyPartyPathParameterFields(),
                getPartyLikeDtoResponseFields(),
                PartyLikeDto.Response.class.getName()))
            .when()
            .header(HttpHeaders.AUTHORIZATION , "Bearer " + jwtTokenResponse.getAccessToken())
            .post("/api/v1/parties/{partyId}/like", party.getId());

        response
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("response", Matchers.equalTo(관심모임등록.toString()));
    }

    @Test
    @DisplayName("관심 모임 취소 테스트")
    void partyLikeCancel() throws Exception {
        Member member = createMember("host@gmail.com", "주최자");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse(member.getEmail(), "hello11@@nW");// 신청자로 로그인
        Party party = saveParty(member);// 주최자기준
        savePartyLike(member, party);// 좋아요 등록

        Response response = givenAuth("",
            template.allRestDocumentation("모임 좋아요",
                getApplyPartyPathParameterFields(),
                getPartyLikeDtoResponseFields(),
                PartyLikeDto.Response.class.getName()))
            .when()
            .header(HttpHeaders.AUTHORIZATION , "Bearer " + jwtTokenResponse.getAccessToken())
            .post("/api/v1/parties/{partyId}/like", party.getId());

        response
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("response", Matchers.equalTo(관심모임취소.toString()));
    }

    private void savePartyLike(Member member, Party party) {
        partyLikeRepository.save(PartyLike.builder()
                .member(member)
                .party(party)
                .build());
    }

    @NotNull
    private List<ParameterDescriptorWithType> getApplyPartyPathParameterFields() {
        return List.of(new ParameterDescriptorWithType("partyId").description("모임 id"));
    }

    @NotNull
    private List<ParameterDescriptorWithType> partyPermissionPathParameterFields() {
        return Arrays.asList(
            new ParameterDescriptorWithType("partyId").description("모임 id"),
            new ParameterDescriptorWithType("targetMemberId").description("신청자 id")
        );
    }

    @NotNull
    private List<FieldDescriptor> getPartyApplyDtoResponseFields() {
        return Arrays.asList(
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("id"),
            fieldWithPath("targetMemberId").type(JsonFieldType.NUMBER).description("신청자id"),
            fieldWithPath("accept").type(JsonFieldType.BOOLEAN).description("허락여부"),
            fieldWithPath("partyId").type(JsonFieldType.NUMBER).description("모임id")
        );
    }

    @NotNull
    private List<FieldDescriptor> getPartyLikeDtoResponseFields() {
        return Arrays.asList(
            fieldWithPath("response").description("좋아요결과")
        );
    }

    @NotNull
    private List<FieldDescriptor> getPartyApplyDtoListResponseFields() {
        return Arrays.asList(
            fieldWithPath("content").type(JsonFieldType.ARRAY).description("모임 신청 목록"),
            fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("id"),
            fieldWithPath("content[].targetMemberId").type(JsonFieldType.NUMBER).description("신청자id"),
            fieldWithPath("content[].partyId").type(JsonFieldType.NUMBER).description("주최자id"),
            fieldWithPath("content[].accept").type(JsonFieldType.BOOLEAN).description("허락여부"),

            fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("페이지정보").ignored(),
            fieldWithPath("pageable.sort").type(JsonFieldType.OBJECT).description("정렬정보").ignored(),
            fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("?").ignored(),
            fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("?").ignored(),
            fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("?").ignored(),
            fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("?").ignored(),
            fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("?").ignored(),
            fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("?").ignored(),
            fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("?").ignored(),
            fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("?").ignored(),


            fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막페이지여부"),
            fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫번째페이지여부"),
            fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("총페이지수"),
            fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("총개수"),
            fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현재페이지모임신청수"),
            fieldWithPath("size").type(JsonFieldType.NUMBER).description("?").ignored(),
            fieldWithPath("number").type(JsonFieldType.NUMBER).description("?").ignored(),
            fieldWithPath("sort").type(JsonFieldType.OBJECT).description("?").ignored(),
            fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("?").ignored(),
            fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("?").ignored(),
            fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("?").ignored(),
            fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("null 여부").ignored()
            );
    }

    @NotNull
    private List<ParameterDescriptorWithType> getPartyApplyDtoListRequestParameterFields() {
        return Arrays.asList(
            new ParameterDescriptorWithType("page").description("페이지 번호"),
            new ParameterDescriptorWithType("size").description("요청 개수")
        );
    }
}
