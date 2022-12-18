package com.example.villagerservice.member.api;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.follow.domain.Follow;
import com.example.villagerservice.follow.domain.FollowRepository;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@Import({AuthConfig.class})
class MemberApiControllerIntegratedTest extends BaseDocumentation {

    private RestDocumentationTemplate template = new RestDocumentationTemplate("회원 API");

    @Autowired
    private FollowRepository followRepository;

    @AfterEach
    void clean() {
        followRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 정보 변경 테스트")
    void updateMemberInfoApiTest() throws Exception {
        // given
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        UpdateMemberInfo.Request request = UpdateMemberInfo.Request.builder()
                .nickname("newNickName")
                .introduce("자기소개변경!")
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
        assertThat(members.get(0).getMemberDetail().getIntroduce()).isEqualTo("자기소개변경!");
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

    @Test
    @DisplayName("회원 마이페이지 조회 테스트")
    void getMyPageApiTest() throws Exception {
        // given
        Member member = createToMember("member@gmail.com", "member");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("member@gmail.com", "hello11@@nW");

        Member fromMember = createToMember("fromMember@gmail.com", "fromMember");
        Member toMember = createToMember("fromMember1@gmail.com", "fromMember1");

        followRepository.save(Follow.createFollow(member, fromMember));
        followRepository.save(Follow.createFollow(toMember, member));
        assertThat(followRepository.count()).isEqualTo(2);

        // when & then
        givenAuth("",
                template.responseRestDocumentation(
                        "회원 마이페이지 조회",
                        getMyPageResponseFields(),
                        MemberDetail.Response.class.getName()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .get("/api/v1/members")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("memberId", Matchers.equalTo(member.getId().intValue()))
                .body("nickName", Matchers.equalTo("member"))
                .body("email", Matchers.equalTo("member@gmail.com"))
                .body("introduce", Matchers.equalTo("안녕하세요! 반갑습니다."))
                .body("gender", Matchers.equalTo("남성"))
                .body("birth", Matchers.equalTo("2022-07-07"))
                .body("mannerPoint", Matchers.equalTo(50))
                .body("partyRegisterCount", Matchers.equalTo(0))
                .body("postRegisterCount", Matchers.equalTo(0))
                .body("follow", Matchers.equalTo(1))
                .body("follower", Matchers.equalTo(1))
                .body("followState", Matchers.equalTo(false))
                .body("tags.size()", Matchers.equalTo(2))
                .body("tags[0]", Matchers.equalTo("#봄"))
                .body("tags[1]", Matchers.equalTo("#여름"))
        ;
    }

    @Test
    @DisplayName("회원 상대방 마이페이지 조회 테스트")
    void getOtherMemberPageApiTest() throws Exception {
        // given
        Member loginMember = createToMember("member@gmail.com", "member");

        Member fromMember = createToMember("fromMember@gmail.com", "fromMember");

        followRepository.save(Follow.createFollow(loginMember, fromMember));
        assertThat(followRepository.count()).isEqualTo(1);

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("member@gmail.com", "hello11@@nW");
        // when & then
        givenAuth("",
                template.responseRestDocumentation(
                        "회원 상대방 마이페이지 조회",
                        getMyPageResponseFields(),
                        MemberDetail.Response.class.getName(),
                        getOtherMemberPagePathParameterFields()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .get("/api/v1/members/{memberId}", fromMember.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("memberId", Matchers.equalTo(fromMember.getId().intValue()))
                .body("nickName", Matchers.equalTo("fromMember"))
                .body("email", Matchers.equalTo("fromMember@gmail.com"))
                .body("introduce", Matchers.equalTo("안녕하세요! 반갑습니다."))
                .body("gender", Matchers.equalTo("남성"))
                .body("birth", Matchers.equalTo("2022-07-07"))
                .body("mannerPoint", Matchers.equalTo(50))
                .body("partyRegisterCount", Matchers.equalTo(0))
                .body("postRegisterCount", Matchers.equalTo(0))
                .body("follow", Matchers.equalTo(0))
                .body("follower", Matchers.equalTo(1))
                .body("followState", Matchers.equalTo(true))
                .body("tags.size()", Matchers.equalTo(2))
                .body("tags[0]", Matchers.equalTo("#봄"))
                .body("tags[1]", Matchers.equalTo("#여름"))
        ;
    }

    @Test
    @DisplayName("비밀번호 검증 테스트")
    void passwordValidApiTest() throws JsonProcessingException {
        // given
        Member loginMember = createToMember("member@gmail.com", "member");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("member@gmail.com", "hello11@@nW");

        PasswordValid.Request request = PasswordValid.Request.builder()
                .password("hello11@@nW")
                .build();

        String body = objectMapper.writeValueAsString(request);

        // when & then
        givenAuth(body,
                template.responseRestDocumentation(
                        "비밀번호 검증",
                        getPasswordValidResponseFields(),
                        PasswordValid.Response.class.getName()
                ))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .post("/api/v1/members/password/valid")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("result", Matchers.equalTo(true))
                .body("message", Matchers.equalTo("비밀번호가 일치합니다."))
        ;
    }

    @Test
    @DisplayName("비밀번호 검증 실패 테스트")
    void passwordValidApiFailTest() throws JsonProcessingException {
        // given
        Member loginMember = createToMember("member@gmail.com", "member");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("member@gmail.com", "hello11@@nW");

        PasswordValid.Request request = PasswordValid.Request.builder()
                .password("hello211@@nW")
                .build();

        String body = objectMapper.writeValueAsString(request);

        // when & then
        givenAuth(body,
                template.responseRestDocumentation(
                        "비밀번호 검증",
                        getPasswordValidResponseFields(),
                        PasswordValid.Response.class.getName()
                ))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .post("/api/v1/members/password/valid")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("result", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("비밀번호가 일치하지 않습니다."))
        ;
    }

    @NotNull
    private List<FieldDescriptor> getMyPageResponseFields() {
        return Arrays.asList(
                fieldWithPath("memberId").description("회원 id"),
                fieldWithPath("nickName").description("닉네임"),
                fieldWithPath("email").description("이메일"),
                fieldWithPath("introduce").description("자기소개"),
                fieldWithPath("gender").description("성별"),
                fieldWithPath("birth").description("생년월일"),
                fieldWithPath("mannerPoint").description("매너점수"),
                fieldWithPath("partyRegisterCount").description("모임 등록 수"),
                fieldWithPath("postRegisterCount").description("게시글 등록 수"),
                fieldWithPath("follow").description("팔로우 수"),
                fieldWithPath("follower").description("팔로워 수"),
                fieldWithPath("followState").description("팔로우 상태"),
                fieldWithPath("tags").type(JsonFieldType.ARRAY).description("태그 목록")
        );
    }

    @NotNull
    private List<FieldDescriptor> getPasswordValidResponseFields() {
        return Arrays.asList(
                fieldWithPath("result").description("결과"),
                fieldWithPath("message").description("message")
        );
    }

    private void defaultAssertThat(List<Member> members) {
        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getEmail()).isEqualTo("test@gmail.com");
    }

    @NotNull
    private List<FieldDescriptor> getUpdateMemberInfoRequestFields() {
        return Arrays.asList(
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                fieldWithPath("introduce").type(JsonFieldType.STRING).description("자기소개")
        );
    }

    @NotNull
    private List<FieldDescriptor> getUpdateMemberPasswordRequestFields() {
        return List.of(fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"));
    }

    @NotNull
    private List<FieldDescriptor> getAddMemberAttentionTagRequestFields() {
        return List.of(fieldWithPath("tags").type(JsonFieldType.ARRAY).description("관심태그"));
    }

    @NotNull
    private List<ParameterDescriptorWithType> getOtherMemberPagePathParameterFields() {
        return List.of(new ParameterDescriptorWithType("memberId").description("회원 id"));
    }
}