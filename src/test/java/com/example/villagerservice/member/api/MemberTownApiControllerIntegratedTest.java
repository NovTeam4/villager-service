package com.example.villagerservice.member.api;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberTown;
import com.example.villagerservice.member.domain.MemberTownRepository;
import com.example.villagerservice.member.domain.TownLocation;
import com.example.villagerservice.member.dto.CreateMemberTown;
import com.example.villagerservice.member.dto.FindMemberTownDetail;
import com.example.villagerservice.member.dto.FindMemberTownList;
import com.example.villagerservice.member.dto.UpdateMemberTown;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.town.domain.Town;
import com.example.villagerservice.town.domain.TownRepository;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@Import({AuthConfig.class})
@ActiveProfiles({"town-test"})
class MemberTownApiControllerIntegratedTest extends BaseDocumentation {

    private RestDocumentationTemplate template = new RestDocumentationTemplate("???????????? API");
    @Autowired
    private MemberTownRepository memberTownRepository;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private TownRepository townRepository;

    @BeforeEach
    void clean() {
        partyRepository.deleteAll();
        memberTownRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ?????????")
    void createMemberTownApiTest() throws Exception {
        // given
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        CreateMemberTown.Request request = CreateMemberTown.Request.builder()
                .townId(1L)
                .townName("??????")
                .latitude(33.319789)
                .longitude(126.774678)
                .build();

        String body = objectMapper.writeValueAsString(request);

        // when & then
        givenAuth(body,
                template.requestRestDocumentation(
                        "???????????? ??????",
                        getCreateMemberTownRequestFields(),
                        CreateMemberTown.Request.class.getName()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .post("/api/v1/members/towns")
                .then()
                .statusCode(HttpStatus.OK.value());

        List<MemberTown> memberTowns = memberTownRepository.getMemberTownFetchJoinByTownId(1L);
        assertThat(memberTowns.size()).isEqualTo(1);

        MemberTown memberTown = memberTowns.get(0);
        assertThat(memberTown.getMember().getEmail()).isEqualTo("test@gmail.com");
        assertThat(memberTown.getMember().getMemberDetail().getNickname()).isEqualTo("original");
        assertThat(passwordEncoder.matches("hello11@@nW", memberTown.getMember().getEncodedPassword()))
                .isTrue();
        assertThat(memberTown.getTownName()).isEqualTo("??????");
        assertThat(memberTown.getTownLocation().getLatitude()).isEqualTo(33.319789);
        assertThat(memberTown.getTownLocation().getLongitude()).isEqualTo(126.774678);
        assertThat(memberTown.getTown().getCode()).isEqualTo("5013032026");
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ?????? ?????????")
    void updateMemberTownNameApiTest() throws Exception {
        // given
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        UpdateMemberTown.Request request = UpdateMemberTown.Request.builder()
                .townName("???????????????")
                .main(false)
                .build();

        String body = objectMapper.writeValueAsString(request);
        Long memberTownId = createMemberTown();

        // when & then
        givenAuth(body,
                template.requestRestDocumentation(
                        "?????? ?????? ??????",
                        getUpdateMemberTownRequestFields(),
                        UpdateMemberTown.Request.class.getName(),
                        getUpdateMemberTownPathParameterFields()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .patch("/api/v1/members/towns/{memberTownId}", memberTownId)
                .then()
                .statusCode(HttpStatus.OK.value());

        MemberTown memberTown = memberTownRepository.findAll().get(0);
        assertThat(memberTown.getTownName()).isEqualTo("???????????????");
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ?????????")
    void deleteMemberTownApiTest() throws Exception {
        // given
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();

        Long memberTownId = createMemberTown();

        // when & then
        givenAuth("",
                template.requestRestDocumentation(
                        "?????? ?????? ??????",
                        getDeleteMemberTownPathParameterFields()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .delete("/api/v1/members/towns/{memberTownId}", memberTownId)
                .then()
                .statusCode(HttpStatus.OK.value());

        List<MemberTown> memberTowns = memberTownRepository.findAll();
        assertThat(memberTowns.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????? ?????? ?????????")
    void getMemberTownListApiTest() throws Exception {
        // given
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();

        Long memberTownId1 = createMemberTown();
        Long memberTownId2 = createMemberTown();

        // when & then
        Response response = givenAuth("",
                template.responseRestDocumentation(
                        "???????????? ?????? ??????",
                        getMemberTownListResponseFields(),
                        FindMemberTownList.Response.class.getName()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .get("/api/v1/members/towns");

        response
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("towns.size()", Matchers.equalTo(2))
        ;

        FindMemberTownList.Response findMemberTownList = objectMapper.readValue(response.asString(), FindMemberTownList.Response.class);
        assertThat(findMemberTownList.getTowns().size()).isEqualTo(2);
        assertThat(findMemberTownList.getTowns().get(0).getMemberTownId()).isEqualTo(memberTownId1);
        assertThat(findMemberTownList.getTowns().get(0).getCityName()).isEqualTo("????????????????????? ???????????? ?????????");
        assertThat(findMemberTownList.getTowns().get(0).getTownName()).isEqualTo("?????????");
        assertThat(findMemberTownList.getTowns().get(1).getMemberTownId()).isEqualTo(memberTownId2);
        assertThat(findMemberTownList.getTowns().get(1).getCityName()).isEqualTo("????????????????????? ???????????? ?????????");
        assertThat(findMemberTownList.getTowns().get(1).getTownName()).isEqualTo("?????????");
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????? ?????? ?????????")
    void getMemberTownDetailApiTest() throws Exception {
        // given
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        Long memberTownId = createMemberTown();

        // when & then
        Response response = givenAuth("",
                template.responseRestDocumentation(
                        "???????????? ?????? ??????",
                        getMemberTownDetailResponseFields(),
                        FindMemberTownDetail.Response.class.getName(),
                        getMemberTownDetailPathParameterFields()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .get("/api/v1/members/towns/{memberTownId}", memberTownId);

        response
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("memberTownId", Matchers.equalTo(memberTownId.intValue()))
                .body("townName", Matchers.equalTo("?????????"))
                .body("cityName", Matchers.equalTo("????????????????????? ???????????? ?????????"))
                .body("latitude", Matchers.equalTo(32.0F))
                .body("longitude", Matchers.equalTo(127.0F))
        ;
    }

    @NotNull
    private List<ParameterDescriptorWithType> getMemberTownDetailPathParameterFields() {
        return List.of(new ParameterDescriptorWithType("memberTownId").description("???????????? id"));
    }


    private Long createMemberTown() {

        Member member = memberRepository.findAll().get(0);
        Town town = townRepository.findById(1L).get();

        MemberTown createMemberTown = MemberTown.createMemberTown(member, town,
                "?????????", new TownLocation(32D, 127D));
        memberTownRepository.save(createMemberTown);
        return createMemberTown.getId();
    }

    @NotNull
    private List<FieldDescriptor> getCreateMemberTownRequestFields() {
        return Arrays.asList(
                fieldWithPath("townId").description("?????? id"),
                fieldWithPath("townName").description("????????????"),
                fieldWithPath("latitude").description("??????"),
                fieldWithPath("longitude").description("??????")
        );
    }

    @NotNull
    private List<FieldDescriptor> getUpdateMemberTownRequestFields() {
        return List.of(
                fieldWithPath("townName").type(JsonFieldType.STRING).description("????????????"),
                fieldWithPath("main").description("???????????? ??????"));
    }

    @NotNull
    private List<ParameterDescriptorWithType> getUpdateMemberTownPathParameterFields() {
        return List.of(new ParameterDescriptorWithType("memberTownId").description("???????????? id"));
    }

    @NotNull
    private List<ParameterDescriptorWithType> getDeleteMemberTownPathParameterFields() {
        return List.of(new ParameterDescriptorWithType("memberTownId").description("???????????? id"));
    }

    @NotNull
    private List<FieldDescriptor> getMemberTownListResponseFields() {
        return Arrays.asList(
                fieldWithPath("towns").type(JsonFieldType.ARRAY).description("????????? ?????? ??????"),
                fieldWithPath("towns[].memberTownId").description("id"),
                fieldWithPath("towns[].townName").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("towns[].cityName").description("?????????"),
                fieldWithPath("towns[].createdAt").description("?????????"),
                fieldWithPath("towns[].modifiedAt").description("?????????"),
                fieldWithPath("towns[].main").description("???????????? ??????"),
                fieldWithPath("towns[].latitude").description("??????"),
                fieldWithPath("towns[].longitude").description("??????")
        );
    }

    @NotNull
    private List<FieldDescriptor> getMemberTownDetailResponseFields() {
        return Arrays.asList(
                fieldWithPath("memberTownId").description("???????????? id"),
                fieldWithPath("townName").description("??????"),
                fieldWithPath("cityName").description("????????????"),
                fieldWithPath("latitude").description("??????"),
                fieldWithPath("longitude").description("??????"),
                fieldWithPath("createdAt").description("?????????"),
                fieldWithPath("modifiedAt").description("?????????")
        );
    }
}