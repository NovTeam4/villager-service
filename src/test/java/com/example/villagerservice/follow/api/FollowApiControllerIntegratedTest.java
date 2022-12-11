package com.example.villagerservice.follow.api;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.follow.domain.Follow;
import com.example.villagerservice.follow.domain.FollowRepository;
import com.example.villagerservice.follow.exception.FollowException;
import com.example.villagerservice.follow.service.FollowService;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.dto.LoginMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.villagerservice.follow.exception.FollowErrorCode.FOLLOW_APPLICATION_NOT_FOUND;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Import({AuthConfig.class})
@ActiveProfiles({"test"})
class FollowApiControllerIntegratedTest extends BaseDocumentation {
    private RestDocumentationTemplate template = new RestDocumentationTemplate("팔로우 API");
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private FollowService followService;

    @BeforeEach
    void clean() {
        followRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("팔로잉 테스트")
    void followingApiTest() throws JsonProcessingException {
        // given
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        Long targetMemberId = createToMember("target@gmail.com", "target");

        // when
        givenAuth("",
                template.requestRestDocumentation(
                        "팔로우 신청",
                        getFollowingPathParameterFields()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .post("/api/v1/follow/{followId}", targetMemberId)
                .then()
                .statusCode(HttpStatus.OK.value());

        // then
        Follow follow = followRepository.getFollowFetchJoin(targetMemberId)
                .orElseThrow(() -> new FollowException(FOLLOW_APPLICATION_NOT_FOUND));

        assertThat(follow.getFromMember().getEmail()).isEqualTo("test@gmail.com");
        assertThat(follow.getFromMember().getMemberDetail().getNickname()).isEqualTo("original");
        assertThat(follow.getToMember().getEmail()).isEqualTo("target@gmail.com");
        assertThat(follow.getToMember().getMemberDetail().getNickname()).isEqualTo("target");
    }

    @Test
    @DisplayName("언팔로우 테스트")
    void unFollowingApiTest() throws JsonProcessingException {
        // given
        Long toMemberId = createToMember("toMember@gmail.com", "toMember");
        Long targetMemberId = createToMember("targetMember@gmail.com", "targetMember");

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("toMember@gmail.com", "hello11@@nW");

        followService.following(toMemberId, targetMemberId);
        assertThat(followRepository.findAll().size()).isEqualTo(1);
        // when
        givenAuth("",
                template.requestRestDocumentation(
                        "언팔로우 신청",
                        getUnFollowingPathParameterFields()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .post("/api/v1/unfollow/{unfollowId}", targetMemberId)
                .then()
                .statusCode(HttpStatus.OK.value());

        // then
        assertThat(followRepository.findAll().size()).isEqualTo(0);
    }

    private JwtTokenResponse getJwtTokenResponse(String email, String pass) throws JsonProcessingException {
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

    private Long createToMember(String email, String nickName) {
        Member member = Member.builder()
                .email(email)
                .encodedPassword(passwordEncoder.encode("hello11@@nW"))
                .nickname(nickName)
                .build();
        memberRepository.save(member);
        return member.getId();
    }

    @NotNull
    private List<ParameterDescriptorWithType> getFollowingPathParameterFields() {
        return List.of(new ParameterDescriptorWithType("followId").description("팔로우 id"));
    }

    @NotNull
    private List<ParameterDescriptorWithType> getUnFollowingPathParameterFields() {
        return List.of(new ParameterDescriptorWithType("unfollowId").description("팔로우 id"));
    }
}