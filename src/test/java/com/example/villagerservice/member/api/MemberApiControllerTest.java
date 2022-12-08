package com.example.villagerservice.member.api;

import com.example.villagerservice.config.WithMockCustomMember;
import com.example.villagerservice.member.dto.CreateMemberAttentionTag;
import com.example.villagerservice.member.dto.UpdateMemberInfo;
import com.example.villagerservice.member.dto.UpdateMemberPassword;
import com.example.villagerservice.member.dto.ValidMemberNickname;
import com.example.villagerservice.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.villagerservice.common.exception.CommonErrorCode.DATA_INVALID_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MemberApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberApiControllerTest {
    @MockBean
    private MemberService memberService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockCustomMember
    @DisplayName("회원 정보 잘못 넘겼을 경우 테스트")
    void updateMemberInfoNotFoundMemberTest() throws Exception {
        // given
        UpdateMemberInfo.Request updateMemberInfo = UpdateMemberInfo.Request.builder()
                .nickname("")
                .build();
        String jsonMemberInfoUpdate = objectMapper.writeValueAsString(updateMemberInfo);

        // when & then
        mockMvc.perform(patch("/api/v1/members/info")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMemberInfoUpdate)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andExpect(jsonPath("$.validation.nickname").value("닉네임은 필수입력 값입니다."))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("회원 정보 변경 테스트")
    void updateMemberInfoTest() throws Exception {
        // given
        UpdateMemberInfo.Request updateMemberInfo = UpdateMemberInfo.Request.builder()
                .nickname("닉네임 변경!")
                .build();
        String jsonMemberInfoUpdate = objectMapper.writeValueAsString(updateMemberInfo);

        doNothing()
                .when(memberService)
                .updateMemberInfo(anyString(), any());

        // when & then
        mockMvc.perform(patch("/api/v1/members/info")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMemberInfoUpdate)
                )
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService, times(1)).updateMemberInfo(anyString(), any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "sid71@N", "sid71@Nsid71@Nsid71@N"})
    @DisplayName("비밀번호 변경시 인자 잘못 넘겼을 경우 테스트")
    void updateMemberPasswordNotFoundMemberTest(String password) throws Exception {
        // given
        UpdateMemberPassword.Request updateMemberPassword = UpdateMemberPassword.Request.builder()
                .password(password)
                .build();
        String jsonMemberInfoUpdate = objectMapper.writeValueAsString(updateMemberPassword);

        // when & then
        mockMvc.perform(patch("/api/v1/members/password")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMemberInfoUpdate)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andExpect(jsonPath("$.validation.password").value("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요."))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("비밀번호 변경 테스트")
    void updateMemberPasswordTest() throws Exception {
        // given
        UpdateMemberPassword.Request updateMemberPassword = UpdateMemberPassword.Request.builder()
                .password("ajkls12@aks!")
                .build();
        String jsonMemberInfoUpdate = objectMapper.writeValueAsString(updateMemberPassword);

        doNothing()
                .when(memberService)
                .updateMemberPassword(anyString(), any());

        // when & then
        mockMvc.perform(patch("/api/v1/members/password")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMemberInfoUpdate)
                )
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService, times(1)).updateMemberPassword(anyString(), any());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("회원 탈퇴 테스트")
    void deleteMemberTest() throws Exception {
        // given
        doNothing()
                .when(memberService)
                .deleteMember(anyString());

        // when & then
        mockMvc.perform(delete("/api/v1/members"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService, times(1)).deleteMember(anyString());
    }

    @Test
    @DisplayName("관심태그 추가 시 하나도 입력 안한 경우 테스트")
    void addMemberAttentionTagEmptyTest() throws Exception {
        // given
        CreateMemberAttentionTag.Request tags = CreateMemberAttentionTag.Request.builder()
                .tags(new ArrayList<>())
                .build();
        String jsonMemberInfoUpdate = objectMapper.writeValueAsString(tags);

        // when & then
        mockMvc.perform(post("/api/v1/members/tags")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMemberInfoUpdate)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andExpect(jsonPath("$.validation.tags").value("태그를 최소 하나 이상 입력해주세요."))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("관심태그 추가 테스트")
    void addMemberAttentionTagTest() throws Exception {
        // given
        CreateMemberAttentionTag.Request tags = CreateMemberAttentionTag.Request.builder()
                .tags(Arrays.asList("#바다", "#겨울"))
                .build();
        String jsonMemberInfoUpdate = objectMapper.writeValueAsString(tags);

        doNothing()
                .when(memberService)
                .addAttentionTag(anyString(), any());

        // when & then
        mockMvc.perform(post("/api/v1/members/tags")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMemberInfoUpdate)
                )
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService, times(1)).addAttentionTag(anyString(), any());
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
        mockMvc.perform(get("/api/v1/members/valid/nickname")
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
                .nickname("닉네임_변경")
                .build();
        String body = objectMapper.writeValueAsString(request);

        doNothing()
                .when(memberService)
                .validNickname(any());

        // when & then
        mockMvc.perform(get("/api/v1/members/valid/nickname")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService, times(1)).validNickname(any());
    }
}