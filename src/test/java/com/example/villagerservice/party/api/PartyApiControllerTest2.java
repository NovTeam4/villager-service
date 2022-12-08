package com.example.villagerservice.party.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.villagerservice.party.request.PartyApplyDto;
import com.example.villagerservice.party.service.PartyApplyService;
import com.example.villagerservice.party.service.PartyQueryService;


import java.util.ArrayList;
import java.util.List;

import com.example.villagerservice.party.service.PartyService;
import com.example.villagerservice.party.service.impl.PartyServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PartyApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PartyApiControllerTest2 {
    @MockBean
    private PartyApplyService partyApplyService;
    @MockBean
    private PartyService partyService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("모임 신청 성공")
    void successApplyParty() throws Exception {
        // given
        given(partyApplyService.applyParty(anyString(), anyLong()))
            .willReturn(PartyApplyDto.Response.builder()
                .id(1L)
                .partyId(1L)
                .targetMemberId(1L)
                .isAccept(false)
                .build());

        // when
        // then
        mockMvc.perform(post("/api/v1/parties/1/apply")
            .contentType(MediaType.APPLICATION_JSON)
            .content(""
            ))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("모임 신청 목록 조회 성공")
    void successGetApplyPartyList() throws Exception {
        // given
        List<PartyApplyDto.Response> list = new ArrayList<>();
        list.add(PartyApplyDto.Response.builder()
            .id(1L)
            .targetMemberId(1L)
            .isAccept(false)
            .partyId(1L)
            .build());
        list.add(PartyApplyDto.Response.builder()
            .id(2L)
            .targetMemberId(2L)
            .isAccept(false)
            .partyId(1L)
            .build());
        Page<PartyApplyDto.Response> pageList = new PageImpl<>(list);
        given(partyApplyService.getApplyPartyList(anyLong(), any()))
            .willReturn(pageList);

        // when
        // then
        mockMvc.perform(get("/api/v1/parties/1/apply")
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(print());
    }

}
