package com.example.villagerservice.member.api;

import com.example.villagerservice.common.jwt.JwtTokenProvider;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.exception.MemberException;
import com.example.villagerservice.member.request.MemberAddAttentionTag;
import com.example.villagerservice.member.request.MemberInfoUpdate;
import com.example.villagerservice.member.request.MemberPasswordUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static com.example.villagerservice.common.exception.CommonErrorCode.DATA_INVALID_ERROR;
import static com.example.villagerservice.member.exception.MemberErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles({"test"})
@Transactional
class MemberApiControllerTest {

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

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원 정보 변경 시 회원이 없을 경우 테스트")
    void updateMemberInfoNotFoundMemberTest() throws Exception {
        // given
        MemberInfoUpdate memberInfoUpdate = MemberInfoUpdate.builder()
                .nickname("닉네임 변경!")
                .build();
        String jsonMemberInfoUpdate = objectMapper.writeValueAsString(memberInfoUpdate);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(Member.builder()
                .email("hello@naver.com")
                .nickname("변경전 닉네임!")
                .build(), null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // when & then
        mockMvc.perform(patch("/api/v1/members/info")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMemberInfoUpdate)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(MEMBER_NOT_FOUND.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(MEMBER_NOT_FOUND.getErrorMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 정보 변경 테스트")
    void updateMemberInfoTest() throws Exception {
        // given
        Member member = Member.builder()
                .email("hello@naver.com")
                .nickname("변경전 닉네임!")
                .build();
        memberRepository.save(member);

        MemberInfoUpdate memberInfoUpdate = MemberInfoUpdate.builder()
                .nickname("닉네임 변경!")
                .build();
        String jsonMemberInfoUpdate = objectMapper.writeValueAsString(memberInfoUpdate);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // when & then
        mockMvc.perform(patch("/api/v1/members/info")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMemberInfoUpdate)
                )
                .andExpect(status().isOk())
                .andDo(print());

        Member findMember = memberRepository.findByEmail("hello@naver.com")
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        assertThat(findMember.getMemberDetail().getNickname()).isEqualTo("닉네임 변경!");
    }

    @Test
    @DisplayName("회원 비밀번호 변경 시 회원이 없을 경우 테스트")
    void updateMemberPasswordNotFoundMemberTest() throws Exception {
        // given
        MemberPasswordUpdate memberPasswordUpdate = MemberPasswordUpdate.builder()
                .password("new!!Password@1")
                .build();
        String jsonMemberInfoUpdate = objectMapper.writeValueAsString(memberPasswordUpdate);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(Member.builder()
                .email("hello@naver.com")
                .nickname("변경전 닉네임!")
                .build(), null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // when & then
        mockMvc.perform(patch("/api/v1/members/password")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMemberInfoUpdate)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(MEMBER_NOT_FOUND.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(MEMBER_NOT_FOUND.getErrorMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 비밀번호 변경 시 비밀번호가 동일한 경우 테스트")
    void updateMemberPasswordSamePasswordTest() throws Exception {
        // given
        String password = "new!!Password@1";
        Member member = Member.builder()
                .email("hello@naver.com")
                .nickname("변경전 닉네임!")
                .encodedPassword(passwordEncoder.encode(password))
                .build();
        memberRepository.save(member);

        MemberPasswordUpdate memberPasswordUpdate = MemberPasswordUpdate.builder()
                .password(password)
                .build();
        String jsonMemberInfoUpdate = objectMapper.writeValueAsString(memberPasswordUpdate);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // when & then
        mockMvc.perform(patch("/api/v1/members/password")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMemberInfoUpdate)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(MEMBER_UPDATE_SAME_PASS.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(MEMBER_UPDATE_SAME_PASS.getErrorMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 비밀번호 변경 시 숫자 없을 경우 테스트")
    void updateMemberPasswordValidTest() throws Exception {
        // given
        String password = "default!!Password@1";
        String newPassword = "new!!Password@";
        Member member = Member.builder()
                .email("hello@naver.com")
                .nickname("변경전 닉네임!")
                .encodedPassword(passwordEncoder.encode(password))
                .build();
        memberRepository.save(member);

        MemberPasswordUpdate memberPasswordUpdate = MemberPasswordUpdate.builder()
                .password(newPassword)
                .build();

        String jsonMemberInfoUpdate = objectMapper.writeValueAsString(memberPasswordUpdate);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // when & then
        mockMvc.perform(patch("/api/v1/members/password")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMemberInfoUpdate)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 비밀번호 테스트")
    void updateMemberPasswordTest() throws Exception {
        // given
        String password = "default!!Password@1";
        String newPassword = "new!!Password@12";
        Member member = Member.builder()
                .email("hello@naver.com")
                .nickname("변경전 닉네임!")
                .encodedPassword(passwordEncoder.encode(password))
                .build();
        memberRepository.save(member);

        MemberPasswordUpdate memberPasswordUpdate = MemberPasswordUpdate.builder()
                .password(newPassword)
                .build();

        String jsonMemberInfoUpdate = objectMapper.writeValueAsString(memberPasswordUpdate);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // when & then
        mockMvc.perform(patch("/api/v1/members/password")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMemberInfoUpdate)
                )
                .andExpect(status().isOk())
                .andDo(print());

        Member findMember = memberRepository.findByEmail("hello@naver.com")
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        // then
        assertThat(passwordEncoder.matches(newPassword, findMember.getEncodedPassword())).isTrue();
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void deleteMemberTest() throws Exception {
        Member member = Member.builder()
                .email("hello@naver.com")
                .nickname("hello")
                .encodedPassword(passwordEncoder.encode("default!!Password@1"))
                .build();
        memberRepository.save(member);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // when
        mockMvc.perform(delete("/api/v1/members"))
                .andExpect(status().isOk())
                .andDo(print());

        Member findMember = memberRepository.findByEmail("hello@naver.com")
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        // then
        assertThat(findMember.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("관심태그 추가 시 5개 넘을경우 테스트")
    void addMemberAttentionTagMaxOverTest() throws Exception {
        // given
        Member member = Member.builder()
                .email("hello@naver.com")
                .nickname("hello")
                .encodedPassword(passwordEncoder.encode("default!!Password@1"))
                .build();

        memberRepository.save(member);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        MemberAddAttentionTag memberAddAttentionTag = MemberAddAttentionTag.builder()
                .tags(Arrays.asList("#봄", "#여름", "#가을", "#겨울", "#테스트", "태그"))
                .build();

        String jsonMemberAddAttentionTag =
                objectMapper.writeValueAsString(memberAddAttentionTag);

        // when & then
        mockMvc.perform(post("/api/v1/members/tags")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMemberAddAttentionTag))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(MEMBER_TAG_MAX_COUNT.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(MEMBER_TAG_MAX_COUNT.getErrorMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("관심태그 테스트")
    void addMemberAttentionTagTest() throws Exception {
        // given
        Member member = Member.builder()
                .email("hello@naver.com")
                .nickname("hello")
                .encodedPassword(passwordEncoder.encode("default!!Password@1"))
                .build();

        memberRepository.save(member);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        MemberAddAttentionTag memberAddAttentionTag = MemberAddAttentionTag.builder()
                .tags(Arrays.asList("#봄", "#여름", "#가을", "#겨울", "#계절"))
                .build();

        String jsonMemberAddAttentionTag =
                objectMapper.writeValueAsString(memberAddAttentionTag);

        // when
        mockMvc.perform(post("/api/v1/members/tags")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMemberAddAttentionTag))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertThat(member.getTagCollection().getTagCount()).isEqualTo(5);
        assertThat(member.getTagCollection().getTags().get(0).getName()).isEqualTo("#봄");
        assertThat(member.getTagCollection().getTags().get(1).getName()).isEqualTo("#여름");
        assertThat(member.getTagCollection().getTags().get(2).getName()).isEqualTo("#가을");
        assertThat(member.getTagCollection().getTags().get(3).getName()).isEqualTo("#겨울");
        assertThat(member.getTagCollection().getTags().get(4).getName()).isEqualTo("#계절");
    }
}