package com.example.villagerservice.member.api;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.config.redis.RedisRepository;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.dto.CreateMember;
import com.example.villagerservice.member.dto.LoginMember;
import com.example.villagerservice.member.dto.ValidMemberNickname;
import com.example.villagerservice.member.service.MemberService;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;
import java.util.List;

import static com.example.villagerservice.member.domain.Gender.MAN;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@Import({AuthConfig.class})
class AuthApiControllerIntegratedTest extends BaseDocumentation {

    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.access-token-validity-in-seconds}")
    private Long accessTokenValiditySeconds;

    private RestDocumentationTemplate template = new RestDocumentationTemplate("?????? API");

    @Test
    @DisplayName("???????????? ?????????")
    void createMemberApiTest() throws Exception {
        // given
        CreateMember.Request memberCreate = CreateMember.Request.builder()
                .nickname("nickname1")
                .email("hello1@gmail.com")
                .password("helloWorld1!!")
                .gender("MAN")
                .birth("2022-12-13")
                .introduce("??????????????? ???????????????!")
                .build();
        String body = objectMapper.writeValueAsString(memberCreate);

        // when & then
        givenAuthPass(body,
                template.requestRestDocumentation(
                        "????????????",
                        getSignupRequestFields(),
                        CreateMember.Request.class.getName()))
                .when()
                .post("/api/v1/auth/signup")
                .then()
                .statusCode(HttpStatus.OK.value());

        List<Member> members = memberRepository.findAll();

        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getEmail()).isEqualTo("hello1@gmail.com");
        assertThat(passwordEncoder.matches("helloWorld1!!", members.get(0).getEncodedPassword())).isTrue();
        assertThat(members.get(0).getMemberDetail().getNickname()).isEqualTo("nickname1");
        assertThat(members.get(0).getMemberDetail().getGender()).isEqualTo(MAN);
        assertThat(members.get(0).getMemberDetail().getBirthday().getYear()).isEqualTo(2022);
        assertThat(members.get(0).getMemberDetail().getBirthday().getMonth()).isEqualTo(12);
        assertThat(members.get(0).getMemberDetail().getBirthday().getDay()).isEqualTo(13);
        assertThat(members.get(0).getMemberDetail().getIntroduce()).isEqualTo("??????????????? ???????????????!");
    }

    @NotNull
    private static List<FieldDescriptor> getSignupRequestFields() {
        return Arrays.asList(
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("?????????"),
                fieldWithPath("email").description("?????????"),
                fieldWithPath("password").description("????????????"),
                fieldWithPath("gender").description("??????"),
                fieldWithPath("birth").description("????????????"),
                fieldWithPath("introduce").description("????????????")
        );
    }

    @Test
    @DisplayName("?????? ?????????")
    void reissueTest() throws Exception {
        // given
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();

        // when & then
        Response refreshTokenResponse = givenAuthRefreshToken("",
                template.responseRestDocumentation(
                        "?????? ?????????",
                        getJwtTokenResponseFields(),
                        JwtTokenResponse.class.getName()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .header("refresh-token", jwtTokenResponse.getRefreshToken())
                .post("/api/v1/auth/refresh");
        ;

        jwtTokenResponse = objectMapper.readValue(refreshTokenResponse.asString(), JwtTokenResponse.class);
        refreshTokenResponse
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("accessToken", Matchers.equalTo(jwtTokenResponse.getAccessToken()))
                .body("grantType", Matchers.equalTo(jwtTokenResponse.getGrantType()))
                .body("refreshToken", Matchers.equalTo(jwtTokenResponse.getRefreshToken()))
                .body("accessTokenExpirationTime", Matchers.equalTo(jwtTokenResponse.getAccessTokenExpirationTime().intValue()));
    }

    @Test
    @DisplayName("????????? ?????????")
    void loginTest() throws Exception {
        // given
        CreateMember.Request request = CreateMember.Request.builder()
                .email("test5@gamil.com")
                .password("hello11@nW")
                .nickname("testNickname")
                .gender("MAN")
                .birth("2022-12-13")
                .introduce("??????????????? ???????????????!")
                .build();
        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Content-type", "application/json")
                .body(objectMapper.writeValueAsString(request))
                .log().all()
                .post("/api/v1/auth/signup");

        LoginMember.Request login = LoginMember.Request.builder()
                .email("test5@gamil.com")
                .password("hello11@nW")
                .build();

        Response tokenResponse = givenAuthPass(
                objectMapper.writeValueAsString(login),
                template.responseRestDocumentation(
                        "?????????",
                        getJwtTokenResponseFields(),
                        JwtTokenResponse.class.getName()))
                .when()
                .post("/api/v1/auth/login");

        JwtTokenResponse jwtTokenResponse = objectMapper.readValue(tokenResponse.asString(), JwtTokenResponse.class);
        tokenResponse
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("accessToken", Matchers.equalTo(jwtTokenResponse.getAccessToken()))
                .body("grantType", Matchers.equalTo(jwtTokenResponse.getGrantType()))
                .body("refreshToken", Matchers.equalTo(jwtTokenResponse.getRefreshToken()))
                .body("accessTokenExpirationTime", Matchers.equalTo(jwtTokenResponse.getAccessTokenExpirationTime().intValue()));
    }

    @Test
    @DisplayName("???????????? ?????????")
    void logoutTest() throws Exception {
        // given
        CreateMember.Request request = CreateMember.Request.builder()
                .email("test5@gamil.com")
                .password("hello11@nW")
                .nickname("testNickname")
                .gender("MAN")
                .birth("2022-12-13")
                .introduce("??????????????? ???????????????!")
                .build();
        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Content-type", "application/json")
                .body(objectMapper.writeValueAsString(request))
                .log().all()
                .post("/api/v1/auth/signup");

        LoginMember.Request login = LoginMember.Request.builder()
                .email("test5@gamil.com")
                .password("hello11@nW")
                .build();


        Response tokenResponse = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Content-type", "application/json")
                .body(objectMapper.writeValueAsString(login))
                .log().all()
                .post("/api/v1/auth/login");

        assertThat(redisTemplate.opsForValue().get("VILLAGER:test5@gamil.com")).isNotNull();

        JwtTokenResponse jwtTokenResponse = objectMapper.readValue(tokenResponse.asString(), JwtTokenResponse.class);

        givenAuth("",
                template.requestRestDocumentation(
                        "????????????"
                ))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .get("/api/v1/auth/logout")
                .then()
                .statusCode(HttpStatus.OK.value());

        assertThat(redisTemplate.opsForValue().get("VILLAGER:test5@gamil.com")).isNull();
    }

    @Test
    @DisplayName("?????? ????????? ????????? ?????? ?????????")
    void validNicknameApiTest() throws Exception {
        // given
        ValidMemberNickname.Request request = ValidMemberNickname.Request.builder()
                .nickname("??????????????????")
                .build();

        String body = objectMapper.writeValueAsString(request);

        // when & then
        givenAuthPass(body,
                template.requestRestDocumentation(
                        "????????? ????????????",
                        getValidNicknameRequestFields(),
                        ValidMemberNickname.Request.class.getName()))
                .when()
                .post("/api/v1/auth/valid/nickname")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("???????????? ????????? ?????? ?????????")
    void ????????????_?????????_??????_?????????() throws Exception {
        String email = "test@naver.com";
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();

        givenAuth("",
            template.allRestDocumentation("????????? ??????",
                getEmailCertPathParameterFields()))
            .header(HttpHeaders.AUTHORIZATION , "Bearer " + jwtTokenResponse.getAccessToken())
            .when()
            .post("/api/v1/auth/email-cert/{email}", email)
            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @NotNull
    private List<FieldDescriptor> getJwtTokenResponseFields() {
        return Arrays.asList(
                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("access-token"),
                fieldWithPath("grantType").description("type"),
                fieldWithPath("refreshToken").description("refresh-token"),
                fieldWithPath("accessTokenExpirationTime").description("access-token ????????????"),
                fieldWithPath("loginMemberId").description("???????????? ?????? id")
        );
    }

    @NotNull
    private List<FieldDescriptor> getValidNicknameRequestFields() {
        return List.of(fieldWithPath("nickname").type(JsonFieldType.STRING).description("?????????"));
    }

    @NotNull
    private List<ParameterDescriptorWithType> getEmailCertPathParameterFields() {
        return List.of(new ParameterDescriptorWithType("email").description("?????????"));
    }
}