package com.example.villagerservice.follow.api;

import com.example.villagerservice.config.WithMockCustomMember;
import com.example.villagerservice.follow.dto.FollowList;
import com.example.villagerservice.follow.dto.FollowListItem;
import com.example.villagerservice.follow.service.FollowQueryService;
import com.example.villagerservice.follow.service.FollowService;
import com.example.villagerservice.member.dto.UpdateMemberInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FollowApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class FollowApiControllerTest {
    @MockBean
    private FollowService followService;
    @MockBean
    private FollowQueryService followQueryService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockCustomMember
    @DisplayName("팔로잉 테스트")
    void followingTest() throws Exception {
        // when & then
        mockMvc.perform(post("/api/v1/follow/1"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(followService, times(1))
                .following(anyLong(), anyLong());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("언팔로잉 테스트")
    void unFollowingTest() throws Exception {
        // when & then
        mockMvc.perform(post("/api/v1/unfollow/1"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(followService, times(1))
                .unFollowing(anyLong(), anyLong());
    }

    @Test
    @DisplayName("팔로우 랭킹 보기 테스트")
    void getFollowListTest() throws Exception {
        // given
        given(followQueryService.getFollowList(any()))
                .willReturn(FollowList.Response.builder()
                        .pageNumber(1)
                        .follows(Arrays.asList(
                                new FollowListItem("유저1", 30L),
                                new FollowListItem("유저2", 20L),
                                new FollowListItem("유저3", 10L)
                        ))
                        .build());


        // when & then
        mockMvc.perform(get("/api/v1/follows"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.follows[0].nickName").value("유저1"))
                .andExpect(jsonPath("$.follows[0].followCount").value(30))
                .andExpect(jsonPath("$.follows[1].nickName").value("유저2"))
                .andExpect(jsonPath("$.follows[1].followCount").value(20))
                .andExpect(jsonPath("$.follows[2].nickName").value("유저3"))
                .andExpect(jsonPath("$.follows[2].followCount").value(10))
                .andDo(print());

        verify(followQueryService, times(1))
                .getFollowList(any());
    }
}