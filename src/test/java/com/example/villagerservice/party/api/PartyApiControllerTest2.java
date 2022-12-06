package com.example.villagerservice.party.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.villagerservice.party.service.PartyApplyService;
import com.example.villagerservice.party.service.impl.PartyApplyServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PartyApiController.class)
@WebAppConfiguration
public class PartyApiControllerTest2 {
    @MockBean
    private PartyApplyService partyApplyService;
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void successApplyParty() throws Exception {
        // given
        // when

        // then
        mockMvc.perform(post("/api/v1/parties/1/apply"))
            .andExpect(status().isOk())
            .andDo(print());
    }


    @Test
    void successGetApplyPartyList() throws Exception {
        // given
        given(partyApplyService.getApplyPartyList(anyLong(), any()))
            .willReturn(Page.empty());
        // when

        // then
        mockMvc.perform(get("/api/v1/parties/1/apply")
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(print());
    }
}
