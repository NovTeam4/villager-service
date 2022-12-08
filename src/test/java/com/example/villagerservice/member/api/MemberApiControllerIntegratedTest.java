package com.example.villagerservice.member.api;

import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.dto.CreateMemberAttentionTag;
import com.example.villagerservice.member.dto.LoginMember;
import com.example.villagerservice.member.dto.UpdateMemberInfo;
import com.example.villagerservice.member.dto.UpdateMemberPassword;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@Import({AuthConfig.class})
class MemberApiControllerIntegratedTest extends BaseDocumentation {

    private RestDocumentationTemplate template = new RestDocumentationTemplate("회원 API");

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 정보 변경 테스트")
    void updateMemberInfoApiTest() throws Exception {
        // given
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        UpdateMemberInfo.Request request = UpdateMemberInfo.Request.builder()
                .nickname("newNickName")
                .build();

        String body = objectMapper.writeValueAsString(request);

        // when & then
        givenAuth(body,
                template.requestRestDocumentation(
                        "회원정보 변경",
                        getUpdateMemberInfoRequestFields(),
                        UpdateMemberInfo.Request.class.getName()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .patch("/api/v1/members/info")
                .then()
                .statusCode(HttpStatus.OK.value());

        List<Member> members = memberRepository.findAll();

        defaultAssertThat(members);
        assertThat(members.get(0).getMemberDetail().getNickname()).isEqualTo("newNickName");
    }

    @Test
    @DisplayName("회원 비밀번호 변경 테스트")
    void updateMemberPasswordApiTest() throws Exception {
        // given
        String changePassword = "newkd@@n12";
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        UpdateMemberPassword.Request request = UpdateMemberPassword.Request.builder()
                .password(changePassword)
                .build();

        String body = objectMapper.writeValueAsString(request);

        // when & then
        givenAuth(body,
                template.requestRestDocumentation(
                        "회원 비밀번호 변경",
                        getUpdateMemberPasswordRequestFields(),
                        UpdateMemberPassword.Request.class.getName()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .patch("/api/v1/members/password")
                .then()
                .statusCode(HttpStatus.OK.value());

        List<Member> members = memberRepository.findAll();

        defaultAssertThat(members);
        assertThat(passwordEncoder.matches(changePassword, members.get(0).getEncodedPassword())).isTrue();
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void deleteMemberApiTest() throws Exception {
        // given
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();

        // when & then
        givenAuth("",
                template.requestRestDocumentation(
                        "회원 탈퇴"))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .delete("/api/v1/members")
                .then()
                .statusCode(HttpStatus.OK.value());

        List<Member> members = memberRepository.findAll();

        defaultAssertThat(members);
        assertThat(members.get(0).isDeleted()).isTrue();
    }

    @Test
    @DisplayName("회원 관심태그 추가 테스트")
    void addMemberAttentionTagApiTest() throws Exception {
        // given
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        CreateMemberAttentionTag.Request request = CreateMemberAttentionTag.Request.builder()
                .tags(Arrays.asList("#봄", "#여름", "#가을", "#겨울"))
                .build();

        String body = objectMapper.writeValueAsString(request);

        // when & then
        givenAuth(body,
                template.requestRestDocumentation(
                        "회원 관심태그 추가",
                        getAddMemberAttentionTagRequestFields(),
                        CreateMemberAttentionTag.Request.class.getName()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .post("/api/v1/members/tags")
                .then()
                .statusCode(HttpStatus.OK.value());

        List<Member> members = memberRepository.findAll();

        defaultAssertThat(members);
        Member member = memberRepository.findByMemberWithTag(members.get(0).getId())
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
        assertThat(member.getTagCollection().getTagCount()).isEqualTo(4);
    }

    private void defaultAssertThat(List<Member> members) {
        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getEmail()).isEqualTo("test@gmail.com");
    }

    private void createMember() {
        Member member = Member.builder()
                .email("test@gmail.com")
                .encodedPassword(passwordEncoder.encode("hello11@@nW"))
                .nickname("original")
                .build();
        memberRepository.save(member);
    }

//    private JwtTokenResponse getJwtTokenResponse() throws JsonProcessingException {
//        createMember();
//        LoginMember.Request login = LoginMember.Request.builder()
//                .email("test@gmail.com")
//                .password("hello11@@nW")
//                .build();
//
//        Response response = given()
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .header("Content-type", "application/json")
//                .body(objectMapper.writeValueAsString(login))
//                .log().all()
//                .post("/api/v1/auth/login");
//
//
//        return objectMapper.readValue(response.asString(), JwtTokenResponse.class);
//    }

    @NotNull
    private List<FieldDescriptor> getUpdateMemberInfoRequestFields() {
        return List.of(fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"));
    }

    @NotNull
    private List<FieldDescriptor> getUpdateMemberPasswordRequestFields() {
        return List.of(fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"));
    }

    @NotNull
    private List<FieldDescriptor> getAddMemberAttentionTagRequestFields() {
        return List.of(fieldWithPath("tags").type(JsonFieldType.ARRAY).description("관심태그"));
    }
}