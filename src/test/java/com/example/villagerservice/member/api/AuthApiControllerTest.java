package com.example.villagerservice.member.api;

import com.example.villagerservice.config.security.provider.JwtTokenProvider;
import com.example.villagerservice.member.request.MemberCreate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles({"test"})
class AuthApiControllerTest {

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
        String memberJson = createMember("    ", "test@gamil.com", "Shwi18a78!!");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(memberJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value("MEMBER_VALIDATE_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value("유효성 검사에 실패하였습니다."))
                .andExpect(jsonPath("$.validation.nickname").value("닉네임은 필수입력 값입니다."))
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "     ", "notformat@@"})
    @DisplayName("이메일 형식이 아닐 경우 테스트")
    void createMemberApiEmailBlankTest(String email) throws Exception {
        // given
        String memberJson = createMember("nickname1", email, "Shwi18a78!!");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(memberJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value("MEMBER_VALIDATE_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value("유효성 검사에 실패하였습니다."))
                .andExpect(jsonPath("$.validation.email").value("유효하지 않은 이메일 형식입니다."))
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "     ", "ajsidk2", "Test1!!", "TesT12!!Test12!!!"})
    @DisplayName("비밀번호 형식이 맞지 않을경우 테스트")
    void createMemberApiPasswordNotFormatTest(String password) throws Exception {
        // given
        String memberJson = createMember("nickname1", "test@gmail.com", password);

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(memberJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value("MEMBER_VALIDATE_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value("유효성 검사에 실패하였습니다."))
                .andExpect(jsonPath("$.validation.password").value("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 테스트")
    void createMemberApiTest() throws Exception {
        // given
        String memberJson = createMember("nickname1", "test@gmail.com", "helloWorld1!!");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(memberJson)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }


    private String createMember(String nickname, String email, String pass) throws JsonProcessingException {
        MemberCreate memberCreate = MemberCreate.builder()
                .nickname(nickname)
                .email(email)
                .password(pass)
                .build();

        return objectMapper.writeValueAsString(memberCreate);
    }
}