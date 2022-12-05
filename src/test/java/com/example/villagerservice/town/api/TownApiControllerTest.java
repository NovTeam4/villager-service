package com.example.villagerservice.town.api;

import com.example.villagerservice.common.jwt.JwtTokenProvider;
import com.example.villagerservice.town.dto.TownList;
import com.example.villagerservice.town.dto.TownListDetail;
import com.example.villagerservice.town.service.TownQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TownApiController.class)
class TownApiControllerTest {

    @TestConfiguration
    static class AuthApiControllerTestConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf()
                    .disable();

            return http.build();
        }

        @MockBean
        private JwtTokenProvider jwtTokenProvider;

        @MockBean
        private PasswordEncoder passwordEncoder;
    }
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
                        .details(Arrays.asList(
                                TownListDetail.builder()
                                        .name("서울 용산구")
                                        .code("1234567890")
                                        .latitude(37.123123)
                                        .longitude(126.13532)
                                        .build(),
                                TownListDetail.builder()
                                        .name("서울 용산구2")
                                        .code("1234567891")
                                        .latitude(37.1223)
                                        .longitude(126.532)
                                        .build()))
                        .build());

        // when
        // then
        mockMvc.perform(get("/api/v1/towns/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new TownList.LocationRequest()
                        ))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("details[0].name").value("서울 용산구"))
                .andExpect(jsonPath("details[0].code").value("1234567890"))
                .andExpect(jsonPath("details[0].latitude").value(37.123123))
                .andExpect(jsonPath("details[0].longitude").value(126.13532))
                .andExpect(jsonPath("details[1].name").value("서울 용산구2"))
                .andExpect(jsonPath("details[1].code").value("1234567891"))
                .andExpect(jsonPath("details[1].latitude").value(37.1223))
                .andExpect(jsonPath("details[1].longitude").value(126.532))
                .andDo(print());

    }

    @Test
    @DisplayName("근처동네 동,읍,면으로 조회 테스트")
    void getTownListWithNameTest() throws Exception {
        // given
        given(townQueryService.getTownListWithName(any()))
                .willReturn(TownList.Response.builder()
                        .details(Arrays.asList(
                                TownListDetail.builder()
                                        .name("서울 용산구")
                                        .code("1234567890")
                                        .latitude(37.123123)
                                        .longitude(126.13532)
                                        .build(),
                                TownListDetail.builder()
                                        .name("서울 용산구2")
                                        .code("1234567891")
                                        .latitude(37.1223)
                                        .longitude(126.532)
                                        .build()))
                        .build());

        // when
        // then
        mockMvc.perform(get("/api/v1/towns/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new TownList.NameRequest()
                        ))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("details[0].name").value("서울 용산구"))
                .andExpect(jsonPath("details[0].code").value("1234567890"))
                .andExpect(jsonPath("details[0].latitude").value(37.123123))
                .andExpect(jsonPath("details[0].longitude").value(126.13532))
                .andExpect(jsonPath("details[1].name").value("서울 용산구2"))
                .andExpect(jsonPath("details[1].code").value("1234567891"))
                .andExpect(jsonPath("details[1].latitude").value(37.1223))
                .andExpect(jsonPath("details[1].longitude").value(126.532))
                .andDo(print());

    }
}