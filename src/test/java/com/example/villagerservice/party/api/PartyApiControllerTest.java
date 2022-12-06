package com.example.villagerservice.party.api;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.example.villagerservice.party.exception.PartyErrorCode.PARTY_NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.villagerservice.common.jwt.JwtTokenProvider;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.repository.PartyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles({"test"})
@Transactional
public class PartyApiControllerTest {

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

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("모임 등록 시 , 회원이 없을 경우")
    void createPartyWithoutMemberTest() throws Exception {

        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("홍길동")
                .build();

        PartyDTO.Request partyRequest = PartyDTO.Request.builder()
                .partyName("test-party")
                .score(100)
                .startDt(LocalDateTime.now())
                .endDt(LocalDateTime.now())
                .amount(1000)
                .build();

        String value = objectMapper.writeValueAsString(partyRequest);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        Assertions.assertThatThrownBy(() -> this.mockMvc.perform(post("/api/v1/parties")
                        .contentType(APPLICATION_JSON)
                        .content(value))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(MEMBER_NOT_FOUND.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(MEMBER_NOT_FOUND.getErrorMessage()))
                .andDo(print()));
    }

    @Test
    @DisplayName("모임 등록 테스트")
    void createParty() throws Exception {


        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("홍길동")
                .build();

        PartyDTO.Request partyRequest = PartyDTO.Request.builder()
                .partyName("test-party")
                .score(100)
                .startDt(LocalDateTime.now())
                .endDt(LocalDateTime.now())
                .amount(1000)
                .build();

        memberRepository.save(member);

        String value = objectMapper.writeValueAsString(partyRequest);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        mockMvc.perform(post("/api/v1/parties")
                .contentType(APPLICATION_JSON)
                .content(value))
                .andExpect(status().isOk())
                .andDo(print());


        Optional<Party> optionalParty = partyRepository.findById(member.getId());

        Assertions.assertThat(optionalParty.get().getPartyName()).isEqualTo(partyRequest.getPartyName());
        Assertions.assertThat(optionalParty.get().getAmount()).isEqualTo(partyRequest.getAmount());
        Assertions.assertThat(optionalParty.get().getScore()).isEqualTo(partyRequest.getScore());
    }

    @Test
    @DisplayName("모임 조회 시 , 모임이 없을 경우")
    void getPartyWithoutParty() throws Exception {

        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("홍길동")
                .build();

        PartyDTO.Request partyRequest = PartyDTO.Request.builder()
                .partyName("test-party")
                .score(100)
                .startDt(LocalDateTime.now())
                .endDt(LocalDateTime.now())
                .amount(1000)
                .build();

        Party party = Party.createParty(partyRequest.getPartyName(), partyRequest.getScore(), partyRequest.getStartDt(), partyRequest.getEndDt(), partyRequest.getAmount(), member);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        Assertions.assertThatThrownBy(() -> this.mockMvc.perform(get("/api/v1/parties/" + party.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(PARTY_NOT_FOUND.getErrorCode()))
                .andExpect(jsonPath("$.errorMessage").value(PARTY_NOT_FOUND.getErrorMessage()))
                .andDo(print()));
    }

    @Test
    @DisplayName("모임 조회 테스트")
    void getParty() throws Exception {

        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("홍길동")
                .build();

        PartyDTO.Request partyRequest = PartyDTO.Request.builder()
                .partyName("test-party")
                .score(100)
                .startDt(LocalDateTime.now())
                .endDt(LocalDateTime.now())
                .amount(1000)
                .build();

        Party party = Party.createParty(partyRequest.getPartyName(), partyRequest.getScore(), partyRequest.getStartDt(), partyRequest.getEndDt(), partyRequest.getAmount(), member);

        memberRepository.save(member);
        partyRepository.save(party);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        mockMvc.perform(get("/api/v1/parties/" + party.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print());

        Party findParty = partyRepository.findById(party.getId()).get();

        Assertions.assertThat(findParty.getPartyName()).isEqualTo(party.getPartyName());
        Assertions.assertThat(findParty.getMember()).isEqualTo(party.getMember());
    }


}
