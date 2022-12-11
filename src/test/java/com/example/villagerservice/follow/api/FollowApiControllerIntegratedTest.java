package com.example.villagerservice.follow.api;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.follow.domain.Follow;
import com.example.villagerservice.follow.domain.FollowRepository;
import com.example.villagerservice.follow.dto.FollowList;
import com.example.villagerservice.follow.exception.FollowException;
import com.example.villagerservice.follow.service.FollowQueryService;
import com.example.villagerservice.follow.service.FollowService;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.dto.LoginMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
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
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.villagerservice.follow.exception.FollowErrorCode.FOLLOW_APPLICATION_NOT_FOUND;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@Import({AuthConfig.class})
@ActiveProfiles({"test"})
class FollowApiControllerIntegratedTest extends BaseDocumentation {
    private RestDocumentationTemplate template = new RestDocumentationTemplate("팔로우 API");
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private FollowService followService;
    @Autowired
    private FollowQueryService followQueryService;

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

    @Test
    @DisplayName("팔로우 랭킹보기 테스트")
    void getFollowListApiTest() throws JsonProcessingException {
        // given
        List<Long> memberIds = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            memberIds.add(createToMember(String.format("%s@gmail.com", i + 1),
                    String.format("%snickName", i + 1)));
        }

        for (int i = 1; i <= 5; i++) {
            followService.following(memberIds.get(i), memberIds.get(0));
        }

        for (int i = 6; i <= 8; i++) {
            followService.following(memberIds.get(i), memberIds.get(1));
        }

        for (int i = 8; i <= 9; i++) {
            followService.following(memberIds.get(i), memberIds.get(2));
        }

        assertThat(followRepository.findAll().size()).isEqualTo(10);

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("1@gmail.com", "hello11@@nW");

        FollowList.Request request = FollowList.Request.builder()
                .page(1)
                .size(20)
                .build();

        // when
        Response response = givenAuth("",
                template.allRestDocumentation(
                        "팔로우 랭킹 보기",
                        getFollowListRequestParameterFields(),
                        FollowList.Request.class.getName(),
                        getFollowListResponseFields(),
                        FollowList.Response.class.getName()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .get("/api/v1/follows?page={page}&size={size}", request.getPage(), request.getSize());

        // then
        response
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("follows.size()", Matchers.equalTo(3));
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

    @NotNull
    private List<ParameterDescriptorWithType> getFollowListRequestParameterFields() {
        return Arrays.asList(
                new ParameterDescriptorWithType("page").description("페이지 번호"),
                new ParameterDescriptorWithType("size").description("요청 개수")
        );
    }

    @NotNull
    private List<FieldDescriptor> getFollowListResponseFields() {
        return Arrays.asList(
                fieldWithPath("pageNumber").description("페이지 번호"),
                fieldWithPath("follows").type(JsonFieldType.ARRAY).description("팔로우 랭킹 목록"),
                fieldWithPath("follows[].nickName").description("별칭"),
                fieldWithPath("follows[].followCount").description("팔로우 수")
        );
    }
}