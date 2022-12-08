package com.example.villagerservice.member.api;

import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.config.redis.RedisRepository;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.dto.CreateMember;
import com.example.villagerservice.member.dto.LoginMember;
import com.example.villagerservice.member.dto.ValidMemberNickname;
import com.example.villagerservice.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private MemberService memberService;

    @Value("${jwt.access-token-validity-in-seconds}")
    private Long accessTokenValiditySeconds;

    private RestDocumentationTemplate template = new RestDocumentationTemplate("Auth api");

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void createMemberApiTest() throws Exception {
        // given
        CreateMember.Request memberCreate = CreateMember.Request.builder()
                .nickname("nickname1")
                .email("hello1@gmail.com")
                .password("helloWorld1!!")
                .gender("MAN")
                .year(2022)
                .month(12)
                .day(5)
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
        assertThat(members.get(0).getMemberDetail().getBirthday().getDay()).isEqualTo(5);
    }

    @NotNull
    private static List<FieldDescriptor> getSignupRequestFields() {
        return Arrays.asList(
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                fieldWithPath("email").description("이메일"),
                fieldWithPath("password").description("비밀번호"),
                fieldWithPath("gender").description("성별"),
                fieldWithPath("year").description("년"),
                fieldWithPath("month").description("월"),
                fieldWithPath("day").description("일")
        );
    }

    @Test
    @DisplayName("토큰 재요청")
    void reissueTest() throws Exception {
        // given
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();

        // when & then
        Response refreshTokenResponse = givenAuth("",
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
                .year(2022)
                .month(12)
                .day(8)
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
                fieldWithPath("accessTokenExpirationTime").description("access-token 만료시간")
        );
    }

    @NotNull
    private List<FieldDescriptor> getValidNicknameRequestFields() {
        return List.of(fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"));
    }

    private void createMember() {
        Member member = Member.builder()
                .email("test@gmail.com")
                .encodedPassword(passwordEncoder.encode("hello11@@nW"))
                .nickname("original")
                .build();
        memberRepository.save(member);
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
}