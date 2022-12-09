package com.example.villagerservice.member.api;

import com.example.villagerservice.config.WithMockCustomMember;
import com.example.villagerservice.member.dto.CreateMemberTown;
import com.example.villagerservice.member.dto.UpdateMemberTown;
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

import static com.example.villagerservice.common.exception.CommonErrorCode.DATA_INVALID_ERROR;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MemberTownApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberTownApiControllerTest {
    @MockBean
    private MemberTownService memberTownService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockCustomMember
    @DisplayName("동네 추가 시 동네 id가 없는 경우 테스트")
    void createMemberTownNotIdExistTest() throws Exception {
        // given
        CreateMemberTown.Request memberTown = CreateMemberTown.Request.builder()
                .townId(0L)
                .townName("동네")
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
                .andExpect(jsonPath("$.validation.townId").value("동네 id는 필수 입력값 입니다."))
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "일", "가나다라마바사아자"})
    @WithMockCustomMember
    @DisplayName("동네 추가 시 동네별칭 포맷이 맞지않을 경우 테스트")
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
                .andExpect(jsonPath("$.validation.townName").value("동네 별칭은 2~8글자 사이로 입력해주세요."))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("동네 추가 시 위도가 맞지않을 경우 테스트")
    void createMemberTownLatitudeNotFormatTest() throws Exception {
        // given
        CreateMemberTown.Request memberTown = CreateMemberTown.Request.builder()
                .townId(1L)
                .townName("홍길동")
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
                .andExpect(jsonPath("$.validation.latitude").value("위도 값을 확인해주세요."))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("동네 추가 시 경도가 맞지않을 경우 테스트")
    void createMemberTownLongitudeNotFormatTest() throws Exception {
        // given
        CreateMemberTown.Request memberTown = CreateMemberTown.Request.builder()
                .townId(1L)
                .townName("홍길동")
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
                .andExpect(jsonPath("$.validation.longitude").value("경도 값을 확인해주세요."))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("동네 추가 전체 포맷이 맞지않을 경우 테스트")
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
                .andExpect(jsonPath("$.validation.townId").value("동네 id는 필수 입력값 입니다."))
                .andExpect(jsonPath("$.validation.townName").value("동네 별칭은 2~8글자 사이로 입력해주세요."))
                .andExpect(jsonPath("$.validation.latitude").value("위도 값을 확인해주세요."))
                .andExpect(jsonPath("$.validation.longitude").value("경도 값을 확인해주세요."))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("동네 추가 테스트")
    void createMemberTownTest() throws Exception {
        // given
        CreateMemberTown.Request memberTown = CreateMemberTown.Request.builder()
                .townId(1L)
                .townName("우리집")
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
    @ValueSource(strings = {"", " ", "일", "가나다라마바사아자"})
    @WithMockCustomMember
    @DisplayName("동네 별칭 변경 시 포맷이 맞지 않을경우 테스트")
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
                .andExpect(jsonPath("$.validation.townName").value("동네 별칭은 2~8글자 사이로 입력해주세요."))
                .andDo(print());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("동네 별칭 변경 테스트")
    void updateMemberTownNameTest() throws Exception {
        // given
        UpdateMemberTown.Request updateMemberTown = UpdateMemberTown.Request.builder()
                .townName("새로운변경")
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
                .updateMemberTownName(anyLong(), any());
    }
}