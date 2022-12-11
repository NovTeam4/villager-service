package com.example.villagerservice.follow.api;

import com.example.villagerservice.config.WithMockCustomMember;
import com.example.villagerservice.follow.service.FollowService;
import com.example.villagerservice.member.api.MemberTownApiController;
import com.example.villagerservice.member.dto.CreateMemberTown;
import com.example.villagerservice.member.service.MemberTownService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.villagerservice.common.exception.CommonErrorCode.DATA_INVALID_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FollowApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class FollowApiControllerTest {
    @MockBean
    private FollowService followService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockCustomMember
    @DisplayName("팔로잉 테스트")
    void createMemberTownNotIdExistTest() throws Exception {
        // when & then
        mockMvc.perform(post("/api/v1/follows/1"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(followService, times(1))
                .following(anyLong(), anyLong());
    }
}