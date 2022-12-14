package com.example.villagerservice.member.api;

import com.example.villagerservice.config.WithMockCustomMember;
import com.example.villagerservice.member.dto.*;
import com.example.villagerservice.member.service.MemberQueryService;
import com.example.villagerservice.member.service.MemberService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
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
    @MockBean
    private MemberQueryService memberQueryService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockCustomMember
    @DisplayName("?????? ?????? ?????? ??? ???????????? 100??? ????????? ?????? ?????????")
    void updateMemberInfoNotFoundMemberTest() throws Exception {
        // given
        UpdateMemberInfo.Request updateMemberInfo = UpdateMemberInfo.Request.builder()
                .nickname("")
                .introduce("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz" +
                        "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz" +
                        "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz")
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
                .andExpect(jsonPath("$.validation.introduce").value("??????????????? 100?????? ????????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("?????? ?????? ?????? ?????????")
    void updateMemberInfoTest() throws Exception {
        // given
        UpdateMemberInfo.Request updateMemberInfo = UpdateMemberInfo.Request.builder()
                .nickname("????????? ??????!")
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
    @DisplayName("???????????? ????????? ?????? ?????? ????????? ?????? ?????????")
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
                .andExpect(jsonPath("$.validation.password").value("??????????????? 8~16??? ?????? ??? ?????????, ??????, ??????????????? ???????????????."))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("???????????? ?????? ?????????")
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
    @DisplayName("?????? ?????? ?????????")
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
    @DisplayName("???????????? ?????? ??? ????????? ?????? ?????? ?????? ?????????")
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
                .andExpect(jsonPath("$.validation.tags").value("????????? ?????? ?????? ?????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("???????????? ?????? ?????????")
    void addMemberAttentionTagTest() throws Exception {
        // given
        CreateMemberAttentionTag.Request tags = CreateMemberAttentionTag.Request.builder()
                .tags(Arrays.asList("#??????", "#??????"))
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

    @Test
    @WithMockCustomMember
    @DisplayName("??????????????? ?????? ?????????")
    void getMyPageTest() throws Exception {
        // given
        MemberDetail.Response mockResponse = MemberDetail.Response.builder()
                .memberId(1L)
                .nickName("??????")
                .email("test@gmail.com")
                .introduce("????????????")
                .mannerPoint(50)
                .partyRegisterCount(0L)
                .postRegisterCount(0L)
                .follow(0L)
                .follower(0L)
                .tags(Arrays.asList("#???", "#??????"))
                .build();

        given(memberQueryService.getMyPage(anyLong()))
                .willReturn(mockResponse);


        // when & then
        mockMvc.perform(get("/api/v1/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(1))
                .andExpect(jsonPath("$.nickName").value("??????"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.introduce").value("????????????"))
                .andExpect(jsonPath("$.mannerPoint").value(50))
                .andExpect(jsonPath("$.partyRegisterCount").value(0))
                .andExpect(jsonPath("$.postRegisterCount").value(0))
                .andExpect(jsonPath("$.follow").value(0))
                .andExpect(jsonPath("$.follower").value(0))
                .andExpect(jsonPath("$.followState").value(false))
                .andExpect(jsonPath("$.tags[0]").value("#???"))
                .andExpect(jsonPath("$.tags[1]").value("#??????"))
                .andDo(print());

        verify(memberQueryService, times(1)).getMyPage(anyLong());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("????????? ??????????????? ?????? ?????????")
    void getOtherMemberPage() throws Exception {
        // given
        MemberDetail.Response mockResponse = MemberDetail.Response.builder()
                .memberId(1L)
                .nickName("??????")
                .email("test@gmail.com")
                .introduce("????????????")
                .mannerPoint(50)
                .partyRegisterCount(0L)
                .postRegisterCount(0L)
                .follow(0L)
                .follower(0L)
                .tags(Arrays.asList("#???", "#??????"))
                .followState(true)
                .build();

        given(memberQueryService.getOtherMyPage(anyLong(), anyLong()))
                .willReturn(mockResponse);


        // when & then
        mockMvc.perform(get("/api/v1/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(1))
                .andExpect(jsonPath("$.nickName").value("??????"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.introduce").value("????????????"))
                .andExpect(jsonPath("$.mannerPoint").value(50))
                .andExpect(jsonPath("$.partyRegisterCount").value(0))
                .andExpect(jsonPath("$.postRegisterCount").value(0))
                .andExpect(jsonPath("$.follow").value(0))
                .andExpect(jsonPath("$.follower").value(0))
                .andExpect(jsonPath("$.followState").value(true))
                .andExpect(jsonPath("$.tags[0]").value("#???"))
                .andExpect(jsonPath("$.tags[1]").value("#??????"))
                .andDo(print());

        verify(memberQueryService, times(1)).getOtherMyPage(anyLong(), anyLong());    }
}