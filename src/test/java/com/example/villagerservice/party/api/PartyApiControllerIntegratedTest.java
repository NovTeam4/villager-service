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
import com.example.villagerservice.party.domain.*;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.dto.PartyListDTO;
import com.example.villagerservice.party.dto.UpdatePartyDTO;
import com.example.villagerservice.party.repository.*;
import com.example.villagerservice.party.dto.PartyApplyDto;
import com.example.villagerservice.party.dto.PartyLikeDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.Response;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
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

    private RestDocumentationTemplate template = new RestDocumentationTemplate("?????? API");

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PartyCommentRepository partyCommentRepository;

    @Autowired
    private PartyApplyRepository partyApplyRepository;

    @Autowired
    private PartyLikeRepository partyLikeRepository;

    @Autowired
    private PartyTagRepository partyTagRepository;

    @BeforeEach
    void clean() {
        partyApplyRepository.deleteAll();
        partyLikeRepository.deleteAll();
        partyRepository.deleteAll();
        memberRepository.deleteAll();
        partyCommentRepository.deleteAll();
        partyTagRepository.deleteAll();
    }

    @AfterEach
    void afterClean() {
        partyCommentRepository.deleteAll();
        partyApplyRepository.deleteAll();
        partyLikeRepository.deleteAll();
        partyRepository.deleteAll();
        memberRepository.deleteAll();
        partyTagRepository.deleteAll();
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    void createParty() throws Exception {

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();

        PartyDTO.Request request = createRequest();

        String value = objectMapper.writeValueAsString(request);


        givenAuth(value,
                template.requestRestDocumentation(
                        "?????? ??????",
                        getCreatePartyDtoRequestFields(),
                        PartyDTO.Request.class.getName()))
                .when()
                .header(HttpHeaders.AUTHORIZATION , "Bearer " + jwtTokenResponse.getAccessToken())
                .post("/api/v1/parties")
                .then()
                .statusCode(HttpStatus.OK.value());

        List<Party> partyList = partyRepository.findAll();
        Assertions.assertThat(partyList.size()).isEqualTo(1);
        Assertions.assertThat(partyList.get(0).getPartyName()).isEqualTo("test-party");
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    void getParty() throws Exception {

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        Member member = createMember("testparty@gmail.com", "?????????");
        Party party = saveParty(member);
        createPartyComment(party);
        Long partyId = party.getId();

        Response response = givenAuth("",
                template.allRestDocumentation("?????? ??????",
                        getPartyPathParameterFields(),
                        getPartyDtoResponseFields(),
                        PartyDTO.Response.class.getName()
                ))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .get("/api/v1/parties/{partyId}", partyId);

        response
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("partyName",Matchers.equalTo("test-party"));

    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    void deleteParty() throws Exception {

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        Member member = createMember("testparty@gmail.com", "?????????");
        Party party = saveParty(member);
        Long partyId = party.getId();

        givenAuth("",
                template.requestRestDocumentation("?????? ??????"))
                .when()
                .header(HttpHeaders.AUTHORIZATION , "Bearer " + jwtTokenResponse.getAccessToken())
                .delete("/api/v1/parties/{partyId}",partyId)
                .then()
                .statusCode(HttpStatus.OK.value());


        List<Party> parties = partyRepository.findAll();
        Assertions.assertThat(parties.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    void updateParty() throws Exception {

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        Member member = createMember("testparty@gmail.com", "?????????");
        Party party = saveParty(member);
        createPartyComment(party);
        Long partyId = party.getId();

        List<PartyTag> newList = new ArrayList<>();
        newList.add(PartyTag.builder().tagName("??????").build());

        UpdatePartyDTO.Request request = UpdatePartyDTO.Request.builder()
                .partyName("update-Test-party")
                .score(1000)
                .startDt(LocalDate.of(2022,12,25))
                .endDt(LocalDate.of(2022,12,25))
                .amount(1000)
                .numberPeople(4)
                .location("?????????")
                .latitude(130.5)
                .longitude(130.5)
                .content("update-test-content")
                .tagList(newList)
                .build();

        String value = objectMapper.writeValueAsString(request);

        System.out.println("partyId = " + partyId);

        Response response = givenAuth(value,
                template.allRestDocumentation("?????? ??????",
                        getPartyPathParameterFields(),
                        getPartyDtoResponseFields(),
                        PartyDTO.Response.class.getName()
                ))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .patch("/api/v1/parties/{partyId}", partyId);

        response
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("partyName",Matchers.equalTo("update-Test-party"));

    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    void getAllParty() throws Exception {

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        Member member = createMember("testparty@gmail.com", "?????????");
        Party party = saveParty(member);
        createPartyComment(party);
        Party party2 = saveParty(member);
        createPartyComment(party2);

        Response response = givenAuth("",
                template.allRestDocumentation("?????? ?????? ??????",
                        getPartyAllPathParameterFields(),
                        getPartyListDtoResponseFields(),
                        PartyListDTO.class.getName()

                ))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .get("/api/v1/parties/{LAT}/{LNT}",127.1,127.1);

        response
                .then()
                .statusCode(HttpStatus.OK.value());

    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    void createPartyComment() throws Exception {

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        Member member = createMember("testparty@gmail.com", "?????????");
        Party party = saveParty(member);

        String value = "test!";

        Response response = givenAuth(value,
                template.requestRestDocumentation("?????? ?????? ??????",
                        getPartyPathParameterFields()
                ))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .post("/api/v1/parties/{partyId}/comment", party.getId());

        PartyComment partyComment = partyCommentRepository.findByParty_id(party.getId());
        Assertions.assertThat(partyComment.getContents()).isEqualTo("test!");
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    void updatePartyComment() throws Exception {

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        Member member = createMember("testparty@gmail.com", "?????????");
        Party party = saveParty(member);
        String value = "update-test!";
        PartyComment partyComment = savePartyComment(party, value);

        Response response = givenAuth(value,
                template.requestRestDocumentation("?????? ?????? ??????",
                        getPartyCommentPathParameterFields()
                ))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .patch("/api/v1/parties/{partyCommentId}/comment", partyComment.getId());

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(value));

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

    private void createPartyComment(Party party) {

        PartyComment partyComment = PartyComment.createPartyComment("test-comment", party);

        partyCommentRepository.save(partyComment);
    }

    private Party saveParty(Member member) {

        PartyDTO.Request request = PartyDTO.Request.builder()
                .partyName("test-party")
                .score(100)
                .startDt(LocalDate.now())
                .endDt(LocalDate.now().plusDays(2))
                .amount(1000)
                .numberPeople(2)
                .location("?????????")
                .latitude(127.1)
                .longitude(127.1)
                .content("test")
                .tagList(new ArrayList<>())
                .build();

        Party party = Party.createParty(request , member);

        request.getTagList().add(PartyTag.builder()
                .tagName("??????")
                .build());

        request.getTagList().add(PartyTag.builder()
                .tagName("??????")
                .build());

        partyRepository.save(party);

        return party;
    }

    private PartyComment savePartyComment(Party party , String contents) {
        PartyComment partyComment = PartyComment.builder()
                .party(party)
                .contents(contents)
                .build();

        partyCommentRepository.save(partyComment);
        return partyComment;
    }

    private static PartyDTO.Request createRequest() {
        List<PartyTag> tagList = new ArrayList<>();

        tagList.add(PartyTag.builder()
                .tagName("??????")
                .build());
        tagList.add(PartyTag.builder()
                .tagName("??????")
                .build());

        PartyDTO.Request request = PartyDTO.Request.builder()
                .partyName("test-party")
                .score(100)
                .startDt(LocalDate.now())
                .endDt(LocalDate.now().plusDays(2))
                .amount(1000)
                .numberPeople(2)
                .location("?????????")
                .latitude(127.1)
                .longitude(127.1)
                .content("test")
                .tagList(tagList)
                .build();

        return request;
    }


    @Test
    @DisplayName("?????? ?????? ?????????")
    void applyParty() throws Exception {
        Member host = createMember("host@gmail.com", "?????????");
        Member applicant = createMember("applicant@gmail.com", "?????????");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse(applicant.getEmail(), "hello11@@nW");// ???????????? ?????????
        Party party = saveParty(host);// ???????????????

        givenAuth("",
            template.allRestDocumentation("?????? ??????",
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
    @DisplayName("?????? ?????? ?????? ?????????")
    void getApplyPartyList() throws Exception {
        Member host = createMember("host@gmail.com", "?????????");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse(host.getEmail(), "hello11@@nW");// ???????????? ?????????
        Party party = saveParty(host);// ????????????
        int page = 0;
        int size = 3;

        // ?????? ?????? 10???
        int applyCnt = 10;
        for(long i = 1; i <= applyCnt; i++){
            savePartyApply(i, party);
        }

        Response response = givenAuth("",
            template.responseRestDocumentation(
                "?????? ?????? ??????",
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
    @DisplayName("?????? ?????? ?????????")
    void partyPermission() throws Exception {
        Member host = createMember("host@gmail.com", "?????????");
        Member applicant = createMember("applicant@gmail.com", "?????????");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse(host.getEmail(), "hello11@@nW");// ???????????? ?????????
        Party party = saveParty(host);// ???????????????
        savePartyApply(applicant.getId(), party);// ????????????

        Response response = givenAuth("",
            template.allRestDocumentation("?????? ??????",
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

    // ?????? ??????
    private void savePartyApply(Long targetMemberId, Party party) {
        partyApplyRepository.save(PartyApply.builder()
                .party(party)
                .isAccept(false)
                .targetMemberId(targetMemberId)
                .build());
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    void partyLike() throws Exception {
        Member member = createMember("host@gmail.com", "?????????");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse(member.getEmail(), "hello11@@nW");// ???????????? ?????????
        Party party = saveParty(member);// ???????????????

        Response response = givenAuth("",
            template.allRestDocumentation("?????? ?????????",
                getApplyPartyPathParameterFields(),
                getPartyLikeDtoResponseFields(),
                PartyLikeDto.Response.class.getName()))
            .when()
            .header(HttpHeaders.AUTHORIZATION , "Bearer " + jwtTokenResponse.getAccessToken())
            .post("/api/v1/parties/{partyId}/like", party.getId());

        response
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("response", Matchers.equalTo(??????????????????.toString()));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    void partyLikeCancel() throws Exception {
        Member member = createMember("host@gmail.com", "?????????");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse(member.getEmail(), "hello11@@nW");// ???????????? ?????????
        Party party = saveParty(member);// ???????????????
        savePartyLike(member, party);// ????????? ??????

        Response response = givenAuth("",
            template.allRestDocumentation("?????? ?????????",
                getApplyPartyPathParameterFields(),
                getPartyLikeDtoResponseFields(),
                PartyLikeDto.Response.class.getName()))
            .when()
            .header(HttpHeaders.AUTHORIZATION , "Bearer " + jwtTokenResponse.getAccessToken())
            .post("/api/v1/parties/{partyId}/like", party.getId());

        response
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("response", Matchers.equalTo(??????????????????.toString()));
    }

    private void savePartyLike(Member member, Party party) {
        partyLikeRepository.save(PartyLike.builder()
                .member(member)
                .party(party)
                .build());
    }

    @NotNull
    private List<ParameterDescriptorWithType> getApplyPartyPathParameterFields() {
        return List.of(new ParameterDescriptorWithType("partyId").description("?????? id"));
    }

    @NotNull
    private List<ParameterDescriptorWithType> getPartyPathParameterFields() {
        return List.of(new ParameterDescriptorWithType("partyId").description("?????? id"));
    }

    @NotNull
    private List<ParameterDescriptorWithType> getPartyCommentPathParameterFields() {
        return List.of(new ParameterDescriptorWithType("partyCommentId").description("???????????? id"));
    }

    @NotNull
    private List<ParameterDescriptorWithType> getPartyAllPathParameterFields() {
        return List.of(new ParameterDescriptorWithType("LNT").description("????????? ??????"),
                new ParameterDescriptorWithType("LAT").description("????????? ??????"));
    }

    @NotNull
    private List<ParameterDescriptorWithType> partyPermissionPathParameterFields() {
        return Arrays.asList(
            new ParameterDescriptorWithType("partyId").description("?????? id"),
            new ParameterDescriptorWithType("targetMemberId").description("????????? id")
        );
    }

    @NotNull
    private List<FieldDescriptor> getPartyApplyDtoResponseFields() {
        return Arrays.asList(
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("id"),
            fieldWithPath("targetMemberId").type(JsonFieldType.NUMBER).description("?????????id"),
            fieldWithPath("accept").type(JsonFieldType.BOOLEAN).description("????????????"),
            fieldWithPath("partyId").type(JsonFieldType.NUMBER).description("??????id")
        );
    }

    @NotNull
    private List<FieldDescriptor> getPartyLikeDtoResponseFields() {
        return Arrays.asList(
            fieldWithPath("response").description("???????????????")
        );
    }

    @NotNull
    private List<FieldDescriptor> getPartyApplyDtoListResponseFields() {
        return Arrays.asList(
            fieldWithPath("content").type(JsonFieldType.ARRAY).description("?????? ?????? ??????"),
            fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("id"),
            fieldWithPath("content[].targetMemberId").type(JsonFieldType.NUMBER).description("?????????id"),
            fieldWithPath("content[].partyId").type(JsonFieldType.NUMBER).description("?????????id"),
            fieldWithPath("content[].accept").type(JsonFieldType.BOOLEAN).description("????????????"),

            fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("???????????????").ignored(),
            fieldWithPath("pageable.sort").type(JsonFieldType.OBJECT).description("????????????").ignored(),
            fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("?").ignored(),
            fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("?").ignored(),
            fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("?").ignored(),
            fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("?").ignored(),
            fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("?").ignored(),
            fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("?").ignored(),
            fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("?").ignored(),
            fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("?").ignored(),


            fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("????????????????????????"),
            fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("????????????????????????"),
            fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("???????????????"),
            fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("?????????"),
            fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("??????????????????????????????"),
            fieldWithPath("size").type(JsonFieldType.NUMBER).description("?").ignored(),
            fieldWithPath("number").type(JsonFieldType.NUMBER).description("?").ignored(),
            fieldWithPath("sort").type(JsonFieldType.OBJECT).description("?").ignored(),
            fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("?").ignored(),
            fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("?").ignored(),
            fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("?").ignored(),
            fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("null ??????").ignored()
            );
    }

    private List<FieldDescriptor> getCreatePartyDtoRequestFields() {
        return List.of(
                fieldWithPath("partyName").type(JsonFieldType.STRING).description("????????????"),
                fieldWithPath("score").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("startDt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                fieldWithPath("endDt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                fieldWithPath("amount").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("numberPeople").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("location").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                fieldWithPath("tagList").type(JsonFieldType.ARRAY).description("?????? ??????"),
                fieldWithPath("tagList[].id").type(JsonFieldType.NUMBER).description("?????? id").ignored(),
                fieldWithPath("tagList[].tagName").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("tagList[].party").type(JsonFieldType.OBJECT).description("????????? ????????? ??????").ignored()
        );

    }

    private List<FieldDescriptor> getPartyDtoResponseFields() {
        return List.of(
                fieldWithPath("partyName").type(JsonFieldType.STRING).description("????????????"),
                fieldWithPath("score").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("startDt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                fieldWithPath("endDt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                fieldWithPath("amount").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("numberPeople").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("location").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                fieldWithPath("tagNameList").type(JsonFieldType.ARRAY).description("?????? ?????? ??????"),
                fieldWithPath("commentList").type(JsonFieldType.ARRAY).description("?????? ?????? ??????"),
                fieldWithPath("commentList[].contents").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                fieldWithPath("commentList[].partyCommentId").type(JsonFieldType.NUMBER).description("?????? ?????? id"),
                fieldWithPath("commentList[].partyId").type(JsonFieldType.NUMBER).description("?????? id"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("????????? ??????"),
                fieldWithPath("mannerPoint").type(JsonFieldType.NUMBER).description("????????? ????????????"),
                fieldWithPath("partyLike").type(JsonFieldType.BOOLEAN).description("?????? ?????????"),
                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("????????? id")
        );
    }

    private List<FieldDescriptor> getPartyListDtoResponseFields() {
        return List.of(

                fieldWithPath("[].partyId").type(JsonFieldType.NUMBER).description("?????? id"),
                fieldWithPath("[].partyName").type(JsonFieldType.STRING).description("????????????"),
                fieldWithPath("[].startDt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                fieldWithPath("[].endDt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("????????? ??????"),
                fieldWithPath("[].content").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                fieldWithPath("[].location").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("[].tagNameList").type(JsonFieldType.ARRAY).description("?????? ?????? ??????"),
                fieldWithPath("[].partyLike").type(JsonFieldType.BOOLEAN).description("?????? ?????????"),
                fieldWithPath("[].memberId").type(JsonFieldType.NUMBER).description("?????? ????????? id")
        );
    }


    @NotNull
    private List<ParameterDescriptorWithType> getPartyApplyDtoListRequestParameterFields() {
        return Arrays.asList(
            new ParameterDescriptorWithType("page").description("????????? ??????"),
            new ParameterDescriptorWithType("size").description("?????? ??????")
        );
    }
}
