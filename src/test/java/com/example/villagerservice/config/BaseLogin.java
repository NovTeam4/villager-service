package com.example.villagerservice.config;

import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.member.domain.*;
import com.example.villagerservice.member.dto.LoginMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseLogin {
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    private final String pass = "hello11@@nW";

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

    protected JwtTokenResponse getJwtTokenResponse(String email, String pass) throws JsonProcessingException {
        LoginMember.Request login = LoginMember.Request.builder()
                .email(email)
                .password(pass)
                .build();

        Response response = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Content-type", "application/json")
                .body(objectMapper.writeValueAsString(login))
                .log().all()
                .post("/api/v1/auth/login");


        return objectMapper.readValue(response.asString(), JwtTokenResponse.class);
    }

    protected Member createToMember(String email, String nickName) {
        Member member = Member.builder()
                .email(email)
                .encodedPassword(passwordEncoder.encode(pass))
                .nickname(nickName)
                .gender(Gender.MAN)
                .birthday(new Birthday(2022, 7, 7))
                .introduce("???????????????! ???????????????.")
                .build();
        member.addMemberAttentionTag(Arrays.asList(new Tag("#???"), new Tag("#??????")));
        return memberRepository.save(member);
    }

    protected List<FieldDescriptor> getContentsConcatPageResponseFields(List<FieldDescriptor> contents) {
        return Stream.concat(contents.stream(), getPageResponseFields().stream())
                .collect(Collectors.toList());
    }
    @NotNull
    private List<FieldDescriptor> getPageResponseFields() {
        return Arrays.asList(
                fieldWithPath("pageable.sort.empty").description("empty"),
                fieldWithPath("pageable.sort.unsorted").description("unsorted"),
                fieldWithPath("pageable.sort.sorted").description("sorted"),
                fieldWithPath("pageable.offset").description("offset"),
                fieldWithPath("pageable.pageSize").description("????????? ?????????"),
                fieldWithPath("pageable.pageNumber").description("????????? ??????"),
                fieldWithPath("pageable.paged").description("paged"),
                fieldWithPath("pageable.unpaged").description("unpaged"),
                fieldWithPath("last").description("????????? ??????"),
                fieldWithPath("totalElements").description("totalElements"),
                fieldWithPath("totalPages").description("totalPages"),
                fieldWithPath("size").description("size"),
                fieldWithPath("number").description("number"),
                fieldWithPath("sort.empty").description("empty"),
                fieldWithPath("sort.sorted").description("sorted"),
                fieldWithPath("sort.unsorted").description("unsorted"),
                fieldWithPath("first").description("first"),
                fieldWithPath("numberOfElements").description("numberOfElements"),
                fieldWithPath("empty").description("numberOfElements")
        );
    }
}
