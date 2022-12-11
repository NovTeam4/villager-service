package com.example.villagerservice.follow.api;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.follow.domain.Follow;
import com.example.villagerservice.follow.domain.FollowRepository;
import com.example.villagerservice.follow.exception.FollowException;
import com.example.villagerservice.member.domain.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.villagerservice.follow.exception.FollowErrorCode.FOLLOW_APPLICATION_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Import({AuthConfig.class})
@ActiveProfiles({"test"})
class FollowApiControllerIntegratedTest extends BaseDocumentation {
    private RestDocumentationTemplate template = new RestDocumentationTemplate("팔로우 API");
    @Autowired
    private FollowRepository followRepository;

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
        Long targetMemberId = createToMember();

        // when
        givenAuth("",
                template.requestRestDocumentation(
                        "팔로잉",
                        getFollowingPathParameterFields()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .post("/api/v1/follows/{followId}", targetMemberId)
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

    private Long createToMember() {
        Member member = Member.builder()
                .email("target@gmail.com")
                .encodedPassword(passwordEncoder.encode("hello11@@nW"))
                .nickname("target")
                .build();
        memberRepository.save(member);
        return member.getId();
    }

    @NotNull
    private List<ParameterDescriptorWithType> getFollowingPathParameterFields() {
        return List.of(new ParameterDescriptorWithType("followId").description("팔로우 id"));
    }
}