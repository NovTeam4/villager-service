package com.example.villagerservice.member.api;

import com.example.villagerservice.config.WithMockCustomMember;
import com.example.villagerservice.member.dto.*;
import com.example.villagerservice.member.service.MemberTownQueryService;
import com.example.villagerservice.member.service.MemberTownService;
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

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.example.villagerservice.common.exception.CommonErrorCode.DATA_INVALID_ERROR;
import static com.example.villagerservice.common.exception.CommonErrorCode.HTTP_REQUEST_METHOD_NOT_SUPPORTED_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MemberTownApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberTownApiControllerTest {
    @MockBean
    private MemberTownService memberTownService;
    @MockBean
    private MemberTownQueryService memberTownQueryService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockCustomMember
    @DisplayName("?????? ?????? ??? ?????? id??? ?????? ?????? ?????????")
    void createMemberTownNotIdExistTest() throws Exception {
        // given
        CreateMemberTown.Request memberTown = CreateMemberTown.Request.builder()
                .townId(0L)
                .townName("??????")
                .latitude(35.1)
                .longitude(127.3)
                .build();

        String body = objectMapper.writeValueAsString(memberTown);

        // when & then
        mockMvc.perform(post("/api/v1/members/towns")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andExpect(jsonPath("$.validation.townId").value("?????? id??? ?????? ????????? ?????????."))
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "???", "???????????????????????????"})
    @WithMockCustomMember
    @DisplayName("?????? ?????? ??? ???????????? ????????? ???????????? ?????? ?????????")
    void createMemberTownNameNotFormatTest(String townName) throws Exception {
        // given
        CreateMemberTown.Request memberTown = CreateMemberTown.Request.builder()
                .townId(1L)
                .townName(townName)
                .latitude(35.1)
                .longitude(127.3)
                .build();

        String body = objectMapper.writeValueAsString(memberTown);

        // when & then
        mockMvc.perform(post("/api/v1/members/towns")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andExpect(jsonPath("$.validation.townName").value("?????? ????????? 2~8?????? ????????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("?????? ?????? ??? ????????? ???????????? ?????? ?????????")
    void createMemberTownLatitudeNotFormatTest() throws Exception {
        // given
        CreateMemberTown.Request memberTown = CreateMemberTown.Request.builder()
                .townId(1L)
                .townName("?????????")
                .latitude(0D)
                .longitude(127.3)
                .build();

        String body = objectMapper.writeValueAsString(memberTown);

        // when & then
        mockMvc.perform(post("/api/v1/members/towns")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andExpect(jsonPath("$.validation.latitude").value("?????? ?????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("?????? ?????? ??? ????????? ???????????? ?????? ?????????")
    void createMemberTownLongitudeNotFormatTest() throws Exception {
        // given
        CreateMemberTown.Request memberTown = CreateMemberTown.Request.builder()
                .townId(1L)
                .townName("?????????")
                .latitude(32.1823)
                .longitude(0D)
                .build();

        String body = objectMapper.writeValueAsString(memberTown);

        // when & then
        mockMvc.perform(post("/api/v1/members/towns")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andExpect(jsonPath("$.validation.longitude").value("?????? ?????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("?????? ?????? ?????? ????????? ???????????? ?????? ?????????")
    void createMemberTownAllNotFormatTest() throws Exception {
        // given
        CreateMemberTown.Request memberTown = CreateMemberTown.Request.builder()
                .townId(0L)
                .townName("")
                .latitude(0D)
                .longitude(0D)
                .build();

        String body = objectMapper.writeValueAsString(memberTown);

        // when & then
        mockMvc.perform(post("/api/v1/members/towns")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andExpect(jsonPath("$.validation.townId").value("?????? id??? ?????? ????????? ?????????."))
                .andExpect(jsonPath("$.validation.townName").value("?????? ????????? 2~8?????? ????????? ??????????????????."))
                .andExpect(jsonPath("$.validation.latitude").value("?????? ?????? ??????????????????."))
                .andExpect(jsonPath("$.validation.longitude").value("?????? ?????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("?????? ?????? ?????????")
    void createMemberTownTest() throws Exception {
        // given
        CreateMemberTown.Request memberTown = CreateMemberTown.Request.builder()
                .townId(1L)
                .townName("?????????")
                .latitude(31.56892)
                .longitude(125.18232)
                .build();

        String body = objectMapper.writeValueAsString(memberTown);

        // when & then
        mockMvc.perform(post("/api/v1/members/towns")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberTownService, times(1))
                .addMemberTown(anyLong(), any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "???", "???????????????????????????"})
    @WithMockCustomMember
    @DisplayName("?????? ?????? ?????? ??? ????????? ?????? ???????????? ?????????")
    void updateMemberTownNameFormatTest(String townName) throws Exception {
        // given
        UpdateMemberTown.Request memberTown = UpdateMemberTown.Request.builder()
                .townName(townName)
                .build();

        String body = objectMapper.writeValueAsString(memberTown);

        // when & then
        mockMvc.perform(patch("/api/v1/members/towns/1")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(DATA_INVALID_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(DATA_INVALID_ERROR.getErrorMessage()))
                .andExpect(jsonPath("$.validation.townName").value("?????? ????????? 2~8?????? ????????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("?????? ?????? ?????? ?????????")
    void updateMemberTownNameTest() throws Exception {
        // given
        UpdateMemberTown.Request updateMemberTown = UpdateMemberTown.Request.builder()
                .townName("???????????????")
                .build();


        String body = objectMapper.writeValueAsString(updateMemberTown);

        // when & then
        mockMvc.perform(patch("/api/v1/members/towns/1")
                        .contentType(APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberTownService, times(1))
                .updateMemberTown(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("?????? ?????? ????????? path variable ????????? ?????? ?????????")
    void deleteMemberTownNotPathVariableTest() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/v1/members/towns"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(HTTP_REQUEST_METHOD_NOT_SUPPORTED_ERROR.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(HTTP_REQUEST_METHOD_NOT_SUPPORTED_ERROR.getErrorMessage()))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("?????? ?????? ?????? ?????????")
    void deleteMemberTownTest() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/v1/members/towns/{member-town-id}", 1))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberTownService, times(1))
                .deleteMemberTown(anyLong(), anyLong());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("????????? ???????????? ?????? ?????????")
    void getMemberTownListTest() throws Exception {

        // given
        given(memberTownQueryService.getMemberTownList(anyLong()))
                .willReturn(FindMemberTownList.Response.builder()
                        .towns(Arrays.asList(
                                new MemberTownListItem(1L, "name1", "city1", "town1", "village1",
                                        LocalDateTime.now(), LocalDateTime.now(), true, 32.555, 127.123),
                                new MemberTownListItem(2L, "name2", "city2", "town2", "village2",
                                        LocalDateTime.now(), LocalDateTime.now(), false, 32.555, 127.123),
                                new MemberTownListItem(3L, "name3", "city3", "town3", "village3",
                                        LocalDateTime.now(), LocalDateTime.now(), true, 32.555, 127.123)
                        ))
                        .build());

        // when & then
        mockMvc.perform(get("/api/v1/members/towns"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.towns[0].memberTownId").value(1L))
                .andExpect(jsonPath("$.towns[0].townName").value("name1"))
                .andExpect(jsonPath("$.towns[0].cityName").value("city1 town1 village1"))
                .andExpect(jsonPath("$.towns[0].main").value(true))
                .andExpect(jsonPath("$.towns[0].latitude").value(32.555))
                .andExpect(jsonPath("$.towns[0].longitude").value(127.123))
                .andExpect(jsonPath("$.towns[1].memberTownId").value(2L))
                .andExpect(jsonPath("$.towns[1].townName").value("name2"))
                .andExpect(jsonPath("$.towns[1].cityName").value("city2 town2 village2"))
                .andExpect(jsonPath("$.towns[1].main").value(false))
                .andExpect(jsonPath("$.towns[1].latitude").value(32.555))
                .andExpect(jsonPath("$.towns[1].longitude").value(127.123))
                .andExpect(jsonPath("$.towns[2].memberTownId").value(3L))
                .andExpect(jsonPath("$.towns[2].townName").value("name3"))
                .andExpect(jsonPath("$.towns[2].cityName").value("city3 town3 village3"))
                .andExpect(jsonPath("$.towns[2].main").value(true))
                .andExpect(jsonPath("$.towns[2].latitude").value(32.555))
                .andExpect(jsonPath("$.towns[2].longitude").value(127.123))
                .andDo(print());

        verify(memberTownQueryService, times(1))
                .getMemberTownList(anyLong());
    }

    @Test
    @DisplayName("????????? ???????????? ?????? ?????? ?????????")
    void getMemberTownDetailTest() throws Exception {
        // given
        given(memberTownQueryService.getMemberTownDetail(anyLong()))
                .willReturn(FindMemberTownDetail.Response.builder()
                        .memberTownId(1L)
                        .townName("??????")
                        .cityName("?????????")
                        .latitude(32.103)
                        .longitude(127.887)
                        .build());

        // when & then
        mockMvc.perform(get("/api/v1/members/towns/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberTownId").value(1L))
                .andExpect(jsonPath("$.townName").value("??????"))
                .andExpect(jsonPath("$.cityName").value("?????????"))
                .andExpect(jsonPath("$.latitude").value(32.103))
                .andExpect(jsonPath("$.longitude").value(127.887))
                .andDo(print());

        verify(memberTownQueryService, times(1))
                .getMemberTownDetail(anyLong());
    }
}
