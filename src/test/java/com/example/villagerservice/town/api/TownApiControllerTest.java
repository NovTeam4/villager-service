package com.example.villagerservice.town.api;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TownApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class TownApiControllerTest {

    @MockBean
    private TownQueryService townQueryService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("근처동네 좌표로 조회 테스트")
    void getTownListWithLocationTest() throws Exception {
        // given
        given(townQueryService.getTownListWithLocation(any()))
                .willReturn(TownList.Response.builder()
                        .towns(Arrays.asList(
                                TownListDetail.builder()
                                        .name("서울 용산구")
                                        .townCode("1234567890")
                                        .latitude(37.123123)
                                        .longitude(126.13532)
                                        .build(),
                                TownListDetail.builder()
                                        .name("서울 용산구2")
                                        .townCode("1234567891")
                                        .latitude(37.1223)
                                        .longitude(126.532)
                                        .build()))
                        .build());

        // when
        // then
        mockMvc.perform(post("/api/v1/towns/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new TownList.LocationRequest()
                        ))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalCount").value(2))
                .andExpect(jsonPath("towns[0].name").value("서울 용산구"))
                .andExpect(jsonPath("towns[0].townCode").value("1234567890"))
                .andExpect(jsonPath("towns[0].latitude").value(37.123123))
                .andExpect(jsonPath("towns[0].longitude").value(126.13532))
                .andExpect(jsonPath("towns[1].name").value("서울 용산구2"))
                .andExpect(jsonPath("towns[1].townCode").value("1234567891"))
                .andExpect(jsonPath("towns[1].latitude").value(37.1223))
                .andExpect(jsonPath("towns[1].longitude").value(126.532))
                .andDo(print());

    }

    @Test
    @DisplayName("근처동네 동,읍,면으로 조회 테스트")
    void getTownListWithNameTest() throws Exception {
        // given
        given(townQueryService.getTownListWithName(any()))
                .willReturn(TownList.Response.builder()
                        .towns(Arrays.asList(
                                TownListDetail.builder()
                                        .name("서울 용산구")
                                        .townCode("1234567890")
                                        .latitude(37.123123)
                                        .longitude(126.13532)
                                        .build(),
                                TownListDetail.builder()
                                        .name("서울 용산구2")
                                        .townCode("1234567891")
                                        .latitude(37.1223)
                                        .longitude(126.532)
                                        .build()))
                        .build());

        // when
        // then
        mockMvc.perform(post("/api/v1/towns/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new TownList.NameRequest()
                        ))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalCount").value(2))
                .andExpect(jsonPath("towns[0].name").value("서울 용산구"))
                .andExpect(jsonPath("towns[0].townCode").value("1234567890"))
                .andExpect(jsonPath("towns[0].latitude").value(37.123123))
                .andExpect(jsonPath("towns[0].longitude").value(126.13532))
                .andExpect(jsonPath("towns[1].name").value("서울 용산구2"))
                .andExpect(jsonPath("towns[1].townCode").value("1234567891"))
                .andExpect(jsonPath("towns[1].latitude").value(37.1223))
                .andExpect(jsonPath("towns[1].longitude").value(126.532))
                .andDo(print());
    }
}