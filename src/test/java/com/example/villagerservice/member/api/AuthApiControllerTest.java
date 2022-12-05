package com.example.villagerservice.member.api;

import com.example.villagerservice.BaseControllerTest;
import com.example.villagerservice.common.jwt.JwtTokenProvider;
import com.example.villagerservice.member.domain.Gender;
import com.example.villagerservice.member.request.MemberCreate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static com.example.villagerservice.common.exception.CommonErrorCode.DATA_INVALID_ERROR;
import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"test"})
class AuthApiControllerTest extends BaseControllerTest {

    @TestConfiguration
    static class AuthApiControllerTestConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf()
                    .disable();

            return http.build();
        }
        @MockBean
        private JwtTokenProvider jwtTokenProvider;

        @MockBean
        private PasswordEncoder passwordEncoder;
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("닉네임을 전달하지 않을 경우 테스트")
    void createMemberApiNickNameBlankTest() throws Exception {
        // given
        String memberJson = createMember("    ", "test@gamil.com", "Shwi18a78!!",
                Gender.MAN, 2022, 12, 5);

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(memberJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andExpect(jsonPath("$.validation.nickname").value("닉네임은 필수입력 값입니다."))
                .andDo(print());


//        given(this.spec)
//                .filter(document(DEFAULT_RESTDOC_PATH)) // API 문서 관련 필터 추가
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .header("Content-type", "application/json")
//                .body(memberJson)
//                .log().all()
//
//                .when()
//                .post("/api/v1/auth/signup")
//                .then()
//                .statusCode(HttpStatus.OK.value())
//                .body("errorCode", Matchers.equalTo(DATA_INVALID_ERROR.getErrorCode()))
//                .body("errorMessage", Matchers.equalTo(DATA_INVALID_ERROR.getErrorMessage()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "     ", "notformat@@"})
    @DisplayName("이메일 형식이 아닐 경우 테스트")
    void createMemberApiEmailBlankTest(String email) throws Exception {
        // given
        String memberJson = createMember("nickname1", email, "Shwi18a78!!",
                Gender.MAN, 2022, 12, 5);

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(memberJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andExpect(jsonPath("$.validation.email").value("유효하지 않은 이메일 형식입니다."))
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "     ", "ajsidk2", "Test1!!", "TesT12!!Test12!!!"})
    @DisplayName("비밀번호 형식이 맞지 않을경우 테스트")
    void createMemberApiPasswordNotFormatTest(String password) throws Exception {
        // given
        String memberJson = createMember("nickname1", "test@gmail.com", password,
                Gender.MAN, 2022, 12, 5);

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(memberJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andExpect(jsonPath("$.validation.password").value("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요."))
                .andDo(print());
    }

    private static final Snippet REQUEST_FIELDS = requestFields(
            fieldWithPath("nickname").type(JsonFieldType.STRING).description("별칭"),
            fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
            fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
            fieldWithPath("gender").type(JsonFieldType.STRING).description("성별"),
            fieldWithPath("year").type(JsonFieldType.NUMBER).description("년도"),
            fieldWithPath("month").type(JsonFieldType.NUMBER).description("달"),
            fieldWithPath("day").type(JsonFieldType.NUMBER).description("일")
    );

    private static final Snippet RESPONSE_FIELDS = responseFields();

    @Test
    @DisplayName("회원가입 테스트")
    void createMemberApiTest() throws Exception {
        // given
        String memberJson = createMember("nickname1", "test@gmail.com", "helloWorld1!!",
                Gender.MAN, 2022, 12, 5);

        // when & then
//        mockMvc.perform(post("/api/v1/auth/signup")
//                        .contentType(APPLICATION_JSON)
//                        .content(memberJson)
//                )
//                .andExpect(status().isOk())
//                .andDo(print());

        given(this.spec)
                .filter(document(DEFAULT_RESTDOC_PATH, REQUEST_FIELDS)) // API 문서 관련 필터 추가
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Content-type", "application/json")
                .body(memberJson)
                .log().all()
                .when()
                .post("/api/v1/auth/signup")
                .then()
                .statusCode(HttpStatus.OK.value())

        ;
    }


    private String createMember(String nickname, String email, String pass, Gender gender,
                                int year, int month, int day) throws JsonProcessingException {
        MemberCreate memberCreate = MemberCreate.builder()
                .nickname(nickname)
                .email(email)
                .password(pass)
                .gender(gender)
                .year(year)
                .month(month)
                .day(day)
                .build();

        return objectMapper.writeValueAsString(memberCreate);
    }
}