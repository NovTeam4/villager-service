package com.example.villagerservice.member.api;

import com.example.villagerservice.common.jwt.JwtTokenProvider;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.request.PartyCreate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.util.NestedServletException;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_UPDATE_SAME_PASS;
import static com.example.villagerservice.party.exception.PartyErrorCode.PARTY_NOT_FOUND_MEMBER;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        PartyCreate partyCreate = PartyCreate.builder()
                .partyName("TestParty")
                .score(20)
                .amount(10000)
                .build();

        Member member = Member.builder()
                .email("hello@naver.com")
                .nickname("Test")
                .build();

        Party party = new Party(partyCreate , member);

        String value = objectMapper.writeValueAsString(party);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        Assertions.assertThatThrownBy(() -> this.mockMvc.perform(post("/api/v1/parties")
                        .contentType(APPLICATION_JSON)
                        .content(value))
                .andExpect(status().isOk())
                .andDo(print())).hasCause(new PartyException(PARTY_NOT_FOUND_MEMBER));

    }

    @Test
    @DisplayName("모임 등록 테스트")
    void createParty() throws Exception {
        PartyCreate partyCreate = PartyCreate.builder()
                .partyName("TestParty")
                .score(20)
                .amount(10000)
                .build();

        Member member = Member.builder()
                .email("hello@naver.com")
                .nickname("Test")
                .build();

        memberRepository.save(member);

        Party party = new Party(partyCreate , member);

        String value = objectMapper.writeValueAsString(party);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        mockMvc.perform(post("/api/v1/parties")
                .contentType(APPLICATION_JSON)
                .content(value))
                .andExpect(status().isOk())
                .andDo(print());


        Optional<Party> optionalParty = partyRepository.findById(member.getId());

        Assertions.assertThat(optionalParty.get().getPartyName()).isEqualTo(party.getPartyName());
        Assertions.assertThat(optionalParty.get().getAmount()).isEqualTo(party.getAmount());
        Assertions.assertThat(optionalParty.get().getScore()).isEqualTo(party.getScore());
    }
}
