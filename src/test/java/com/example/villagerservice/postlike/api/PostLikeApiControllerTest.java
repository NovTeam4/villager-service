package com.example.villagerservice.postlike.api;

import com.example.villagerservice.config.WithMockCustomMember;
import com.example.villagerservice.postlike.service.PostLikeService;
import com.example.villagerservice.town.api.TownApiController;
import com.example.villagerservice.town.dto.TownList;
import com.example.villagerservice.town.dto.TownListDetail;
import com.example.villagerservice.town.service.TownQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
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

@WebMvcTest(PostLikeApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostLikeApiControllerTest {

    @MockBean
    private PostLikeService postLikeService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockCustomMember
    @DisplayName("게시글 좋아요 테스트")
    void postLikeCheckTest() throws Exception {

        // when & then
        mockMvc.perform(post("/api/v1/postlike/1"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(postLikeService, times(1))
                .postLikeCheck(anyLong(), anyLong());
    }
}