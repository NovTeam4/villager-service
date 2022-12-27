package com.example.villagerservice.party.api;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.villagerservice.config.WithMockCustomMember;
import com.example.villagerservice.party.domain.PartyTag;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.dto.UpdatePartyDTO;
import com.example.villagerservice.party.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PartyApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PartyApiControllerTest {
    @MockBean
    private PartyApplyService partyApplyService;
    @MockBean
    private PartyLikeService partyLikeService;
    @MockBean
    private PartyQueryService partyQueryService;
    @MockBean
    private PartyService partyService;

    @MockBean
    private PartyCommentService partyCommentService;

    @MockBean
    private PartyMemberQueryService partyMemberQueryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    @DisplayName("모임 신청 성공")
//    void successApplyParty() throws Exception {
//        // given
//        given(partyApplyService.applyParty(anyString(), anyLong()))
//            .willReturn(PartyApplyDto.Response.builder()
//                .id(1L)
//                .partyId(1L)
//                .targetMemberId(1L)
//                .isAccept(false)
//                .build());
//
//        // when
//        // then
//        mockMvc.perform(post("/api/v1/parties/1/apply")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(""
//            ))
//            .andExpect(status().isOk())
//            .andDo(print());
//    }
//
//    @Test
//    @DisplayName("모임 신청 목록 조회 성공")
//    void successGetApplyPartyList() throws Exception {
//        // given
//        List<PartyApplyDto.Response> list = new ArrayList<>();
//        list.add(PartyApplyDto.Response.builder()
//            .id(1L)
//            .targetMemberId(1L)
//            .isAccept(false)
//            .partyId(1L)
//            .build());
//        list.add(PartyApplyDto.Response.builder()
//            .id(2L)
//            .targetMemberId(2L)
//            .isAccept(false)
//            .partyId(1L)
//            .build());
//        Page<PartyApplyDto.Response> pageList = new PageImpl<>(list);
//        given(partyApplyService.getApplyPartyList(anyLong(), any()))
//            .willReturn(pageList);
//
//        // when
//        // then
//        mockMvc.perform(get("/api/v1/parties/1/apply")
//                .contentType(MediaType.APPLICATION_JSON)
//            )
//            .andExpect(status().isOk())
//            .andDo(print());
//    }

    @Test
    @DisplayName("모임 등록 테스트")
    @WithMockCustomMember
    void createParty() throws Exception {

        PartyDTO.Request request = createRequest();

        String value = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/parties")
                        .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(value))
                .andExpect(status().isOk())
                .andDo(print());

        verify(partyService,times(1)).createParty(anyLong() , any());

    }

    @Test
    @DisplayName("모임 이름 없이 모임 등록 시 , 모임 등록 실패")
    @WithMockCustomMember
    void createPartyWithoutPartyName() throws Exception {

        PartyDTO.Request request = PartyDTO.Request.builder()
                .score(50)
                .startDt(LocalDate.now())
                .endDt(LocalDate.now().plusDays(2))
                .amount(1000)
                .build();

        String value = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/parties")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validation.partyName").value("모임 이름을 입력해주세요."))
                .andDo(print());

    }

    @Test
    @DisplayName("모임 점수 없이 모임 등록 시 , 모임 등록 실패")
    @WithMockCustomMember
    void createPartyWithoutPartyScore() throws Exception {

        PartyDTO.Request request = PartyDTO.Request.builder()
                .partyName("test-party")
                .startDt(LocalDate.now())
                .endDt(LocalDate.now().plusDays(2))
                .amount(1000)
                .build();

        String value = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/parties")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validation.score").value("모임 점수를 입력해주세요."))
                .andDo(print());

    }

    @Test
    @WithMockCustomMember
    @DisplayName("모임 변경 테스트")
    void updateParty() throws Exception {
        Long partyId = 1L;

        UpdatePartyDTO.Request request = UpdatePartyDTO.Request.builder()
                .partyName("updateTest")
                .build();

        String value = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/v1/parties/{partyId}", partyId)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))
                .andExpect(status().isOk())
                .andDo(print());

        verify(partyService,times(1)).updateParty(anyLong() , any() , any());
    }

    @Test
    @DisplayName("모임 삭제 테스트")
    void deleteParty() throws Exception {

        Long partyId = 1L;
        mockMvc.perform(delete("/api/v1/parties/" + partyId))
                .andExpect(status().isOk())
                .andDo(print());

        verify(partyService,times(1)).deleteParty(anyLong());

    }

    @Test
    @WithMockCustomMember
    @DisplayName("모임 조회 테스트")
    void getParty() throws Exception {
        Long partyId = 1L;
        mockMvc.perform(get("/api/v1/parties/" + partyId))
                .andExpect(status().isOk())
                .andDo(print());

        verify(partyService,times(1)).getParty(anyLong() , anyString());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("모임 전체 조회 테스트")
    void getAllParty() throws Exception {

        mockMvc.perform(get("/api/v1/parties/{LAT}/{LNT}" ,127.1,127.1))
                .andExpect(status().isOk())
                .andDo(print());

        verify(partyQueryService,times(1)).getPartyList(anyString() , anyDouble() , anyDouble());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("사용자가 속한 모임 전체 조회 테스트")
    void getAllPartyWithMember() throws Exception {

        mockMvc.perform(get("/api/v1/parties/"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(partyQueryService,times(1)).getAllPartyWithMember(any());
    }

    @Test
    @DisplayName("모임 댓글 테스트")
    @WithMockCustomMember
    void createComment() throws Exception {

        Long partyId = 1L;
        String value = "test";
        mockMvc.perform(post("/api/v1/parties/{partyId}/comment",partyId)
                .characterEncoding("UTF-8")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content(value))
                .andExpect(status().isOk())
                .andDo(print());

        verify(partyCommentService,times(1)).createComment(anyLong() , anyString() , any());
    }

    @Test
    @DisplayName("모임 댓글 변경 테스트")
    void updateComment() throws Exception {

        Long partyCommentId = 1L;
        String value = "update-test";
        mockMvc.perform(patch("/api/v1/parties/{partyId}/comment",partyCommentId)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(value))
                .andExpect(status().isOk())
                .andDo(print());

        verify(partyCommentService,times(1)).updateComment(partyCommentId , value);
    }

    private static PartyDTO.Request createRequest() {
        List<PartyTag> tagList = new ArrayList<>();

        tagList.add(PartyTag.builder()
                .tagName("낚시")
                .build());
        tagList.add(PartyTag.builder()
                .tagName("볼링")
                .build());

        PartyDTO.Request request = PartyDTO.Request.builder()
                .partyName("test-party")
                .score(100)
                .startDt(LocalDate.now())
                .endDt(LocalDate.now().plusDays(2))
                .amount(1000)
                .numberPeople(2)
                .location("수원시")
                .latitude(127.1)
                .longitude(127.1)
                .content("test")
                .tagList(tagList)
                .build();

        return request;
    }

}
