package com.example.villagerservice.member.api;

import com.example.villagerservice.common.jwt.JwtTokenInfoDto;
import com.example.villagerservice.common.jwt.JwtTokenProvider;
import com.example.villagerservice.common.service.MailService;
import com.example.villagerservice.config.WithMockCustomMember;
import com.example.villagerservice.member.dto.CreateMember;
import com.example.villagerservice.member.dto.ValidMemberNickname;
import com.example.villagerservice.member.service.AuthTokenService;
import com.example.villagerservice.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.villagerservice.common.exception.CommonErrorCode.DATA_INVALID_ERROR;
import static com.example.villagerservice.common.jwt.JwtTokenErrorCode.JWT_ACCESS_TOKEN_NOT_EXIST;
import static com.example.villagerservice.common.jwt.JwtTokenErrorCode.JWT_REFRESH_TOKEN_NOT_EXIST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthApiControllerTest {

    @MockBean
    private MemberService memberService;
    @MockBean
    private AuthTokenService authTokenService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private MailService mailService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("닉네임을 전달하지 않을 경우 테스트")
    void createMemberApiNickNameBlankTest() throws Exception {
        // given
        String memberJson = createMember(
                "    ",
                "test@gamil.com",
                "Shwi18a78!!",
                "MAN",
                "2022-12-13");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(memberJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andExpect(jsonPath("$.validation.nickname").value("닉네임은 필수입력 값이며" +
                        ", 공백은 포함될 수 없습니다."))
                .andDo(print());
    }


    @ParameterizedTest
    @ValueSource(strings = {"", "     ", "notformat@@"})
    @DisplayName("이메일 형식이 아닐 경우 테스트")
    void createMemberApiEmailBlankTest(String email) throws Exception {
        // given
        String memberJson = createMember("nickname1", email, "Shwi18a78!!",
                "MAN", "2022-12-13");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(memberJson))
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
                "MAN", "2022-12-13");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(memberJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andExpect(jsonPath("$.validation.password").value("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요."))
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "man1", "w2oman", "", " "})
    @DisplayName("성별을 잘못 넘겼을경우 테스트")
    void createMemberApiGenderNotFormatTest(String gender) throws Exception {
        // given
        String memberJson = createMember("nickname1", "test@gmail.com", "Shwi18a78!!",
                gender, "2022-12-13");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(memberJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andExpect(jsonPath("$.validation.gender").value("성별은 MAN 또는 WOMAN만 가능합니다."))
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"man", "woman", "MAN", "WOMAN"})
    @DisplayName("성별 성공 테스트")
    void createMemberApiGenderTest(String gender) throws Exception {
        // given
        String memberJson = createMember("nickname1", "test@gmail.com", "Shwi18a78!!",
                gender, "2022-12-13");

        doNothing()
                .when(memberService)
                .createMember(any());

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(memberJson))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService, times(1)).createMember(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2022-1-03", "00-12-03", "2022-12-7"})
    @DisplayName("생년월일 잘못 넘겼을 경우 테스트")
    void createMemberApiYearNotFormatTest(String birth) throws Exception {
        // given
        String memberJson = createMember("nickname1", "test@gmail.com", "Shwi18a78!!",
                "MAN", birth);

        doNothing()
                .when(memberService)
                .createMember(any());

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(memberJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andExpect(jsonPath("$.validation.birth").value("생년월일을 확인해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 테스트")
    void createMemberApiTest() throws Exception {
        // given
        String memberJson = createMember("nickname1", "test@gmail.com", "helloWorld1!!",
                "MAN", "2022-12-13");

        // when & then
        doNothing()
                .when(memberService)
                .createMember(any());

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(memberJson))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService, times(1)).createMember(any());
    }

    @Test
    @DisplayName("refresh 토큰 요청 시 access-token이 없을 경우")
    void reissueNotAccessTokenExistTest() throws Exception {
        // given
        // when & then
        mockMvc.perform(post("/api/v1/auth/refresh"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(JWT_ACCESS_TOKEN_NOT_EXIST.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(JWT_ACCESS_TOKEN_NOT_EXIST.getErrorMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("refresh 토큰 요청 시 refresh-token이 없을 경우")
    void reissueNotRefreshTokenExistTest() throws Exception {
        // given
        given(jwtTokenProvider.getAccessToken(any()))
                .willReturn("accessToken");

        // when & then
        mockMvc.perform(post("/api/v1/auth/refresh"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(JWT_REFRESH_TOKEN_NOT_EXIST.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(JWT_REFRESH_TOKEN_NOT_EXIST.getErrorMessage()))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("refresh 토큰 요청 테스트")
    void reissueTest() throws Exception {
        // given
        given(jwtTokenProvider.getAccessToken(any()))
                .willReturn("accessToken");
        given(jwtTokenProvider.getRefreshToken(any()))
                .willReturn("refreshToken");
        given(authTokenService.getReissueTokenInfo(anyLong(), anyString(), anyString()))
                .willReturn(JwtTokenInfoDto
                        .builder()
                        .grantType("Bearer")
                        .accessToken("access-token")
                        .refreshToken("refresh-token")
                        .accessTokenValidTime(50000L)
                        .refreshTokenExpirationTime(50000L)
                        .build());

        // when & then
        mockMvc.perform(post("/api/v1/auth/refresh"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("accessToken").value("access-token"))
                .andExpect(jsonPath("grantType").value("Bearer"))
                .andExpect(jsonPath("refreshToken").value("refresh-token"))
                .andExpect(jsonPath("accessTokenExpirationTime").value(50000L))
                .andDo(print());

        verify(jwtTokenProvider, times(1)).getAccessToken(any());
        verify(jwtTokenProvider, times(1)).getRefreshToken(any());
        verify(authTokenService, times(1))
                .getReissueTokenInfo(anyLong(), anyString(), anyString());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("로그아웃 테스트")
    void logoutTest() throws Exception {
        // given
        given(jwtTokenProvider.getAccessToken(any()))
                .willReturn("accessToken");

        doNothing()
                .when(authTokenService)
                .logout(anyLong(), anyString());


        // when & then
        mockMvc.perform(get("/api/v1/auth/logout"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(jwtTokenProvider, times(1)).getAccessToken(any());
        verify(authTokenService, times(1)).logout(anyLong(), anyString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "       "})
    @DisplayName("닉네임 유효성 검사 시 닉네임은 전달 안한 경우 테스트")
    void validNicknameNotFormatTest(String nickname) throws Exception {
        // given
        ValidMemberNickname.Request request = ValidMemberNickname.Request.builder()
                .nickname(nickname)
                .build();
        String body = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(post("/api/v1/auth/valid/nickname")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andExpect(jsonPath("$.validation.nickname").value("닉네임은 필수입력 값이며," +
                        " 공백은 포함될 수 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("닉네임 유효성 검사 테스트")
    void validNicknameTest() throws Exception {
        // given
        ValidMemberNickname.Request request = ValidMemberNickname.Request.builder()
                .nickname("닉네임_테스트")
                .build();
        String body = objectMapper.writeValueAsString(request);

        doNothing()
                .when(memberService)
                .validNickname(any());

        // when & then
        mockMvc.perform(post("/api/v1/auth/valid/nickname")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService, times(1)).validNickname(any());
    }

    @NotNull
    private String createMember(String nickname, String email, String pass, String gender,
                                String birth) throws JsonProcessingException {
        CreateMember.Request memberCreate = CreateMember.Request.builder()
                .nickname(nickname)
                .email(email)
                .password(pass)
                .gender(gender)
                //.birth(String.format("%d-%02d-%02d", year, month, day))
                .birth(String.format(birth))
                .introduce("안녕하세요 반갑습니다!")
                .build();

        return objectMapper.writeValueAsString(memberCreate);
    }
}