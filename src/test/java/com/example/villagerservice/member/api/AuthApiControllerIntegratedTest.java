package com.example.villagerservice.member.api;

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

    private RestDocumentationTemplate template = new RestDocumentationTemplate("인증 API");

    @Test
    @DisplayName("회원가입 테스트")
    void createMemberApiTest() throws Exception {
        // given
        CreateMember.Request memberCreate = CreateMember.Request.builder()
                .nickname("nickname1")
                .email("hello1@gmail.com")
                .password("helloWorld1!!")
                .gender("MAN")
                .birth("2022-12-13")
                .introduce("안녕하세요 반갑습니다!")
                .build();
        String body = objectMapper.writeValueAsString(memberCreate);

        // when & then
        givenAuthPass(body,
                template.requestRestDocumentation(
                        "회원가입",
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
        assertThat(members.get(0).getMemberDetail().getIntroduce()).isEqualTo("안녕하세요 반갑습니다!");
    }

    @NotNull
    private static List<FieldDescriptor> getSignupRequestFields() {
        return Arrays.asList(
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                fieldWithPath("email").description("이메일"),
                fieldWithPath("password").description("비밀번호"),
                fieldWithPath("gender").description("성별"),
                fieldWithPath("birth").description("생년월일"),
                fieldWithPath("introduce").description("자기소개")
        );
    }

    @Test
    @DisplayName("토큰 재요청")
    void reissueTest() throws Exception {
        // given
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();

        // when & then
        Response refreshTokenResponse = givenAuthRefreshToken("",
                template.responseRestDocumentation(
                        "토큰 재요청",
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
    @DisplayName("로그인 테스트")
    void loginTest() throws Exception {
        // given
        CreateMember.Request request = CreateMember.Request.builder()
                .email("test5@gamil.com")
                .password("hello11@nW")
                .nickname("testNickname")
                .gender("MAN")
                .birth("2022-12-13")
                .introduce("안녕하세요 반갑습니다!")
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
                        "로그인",
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
    @DisplayName("로그아웃 테스트")
    void logoutTest() throws Exception {
        // given
        CreateMember.Request request = CreateMember.Request.builder()
                .email("test5@gamil.com")
                .password("hello11@nW")
                .nickname("testNickname")
                .gender("MAN")
                .birth("2022-12-13")
                .introduce("안녕하세요 반갑습니다!")
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
                        "로그아웃"
                ))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .get("/api/v1/auth/logout")
                .then()
                .statusCode(HttpStatus.OK.value());

        assertThat(redisTemplate.opsForValue().get("VILLAGER:test5@gamil.com")).isNull();
    }

    @Test
    @DisplayName("회원 닉네임 유효성 검사 테스트")
    void validNicknameApiTest() throws Exception {
        // given
        ValidMemberNickname.Request request = ValidMemberNickname.Request.builder()
                .nickname("닉네임테스트")
                .build();

        String body = objectMapper.writeValueAsString(request);

        // when & then
        givenAuthPass(body,
                template.requestRestDocumentation(
                        "닉네임 중복체크",
                        getValidNicknameRequestFields(),
                        ValidMemberNickname.Request.class.getName()))
                .when()
                .get("/api/v1/auth/valid/nickname")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @NotNull
    private List<FieldDescriptor> getJwtTokenResponseFields() {
        return Arrays.asList(
                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("access-token"),
                fieldWithPath("grantType").description("type"),
                fieldWithPath("refreshToken").description("refresh-token"),
                fieldWithPath("accessTokenExpirationTime").description("access-token 만료시간"),
                fieldWithPath("loginMemberId").description("로그인한 회원 id")
        );
    }

    @NotNull
    private List<FieldDescriptor> getValidNicknameRequestFields() {
        return List.of(fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"));
    }
}