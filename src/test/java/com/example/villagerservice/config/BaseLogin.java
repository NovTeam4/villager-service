package com.example.villagerservice.config;

import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.dto.LoginMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseLogin {
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected PasswordEncoder passwordEncoder;

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }
    private void createMember() {
        Member member = Member.builder()
                .email("test@gmail.com")
                .encodedPassword(passwordEncoder.encode("hello11@@nW"))
                .nickname("original")
                .build();
        memberRepository.save(member);
    }
    protected JwtTokenResponse getJwtTokenResponse() throws JsonProcessingException {
        createMember();
        LoginMember.Request login = LoginMember.Request.builder()
                .email("test@gmail.com")
                .password("hello11@@nW")
                .build();

        Response response = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Content-type", "application/json")
                .body(objectMapper.writeValueAsString(login))
                .log().all()
                .post("/api/v1/auth/login");


        return objectMapper.readValue(response.asString(), JwtTokenResponse.class);
    }
}
