package com.example.villagerservice.member.api;

import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.member.domain.MemberTown;
import com.example.villagerservice.member.domain.MemberTownRepository;
import com.example.villagerservice.member.dto.CreateMemberTown;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@Import({AuthConfig.class})
@ActiveProfiles({"town-test"})
class MemberTownApiControllerIntegratedTest extends BaseDocumentation {

    private RestDocumentationTemplate template = new RestDocumentationTemplate("회원 동네 API");
    @Autowired
    private MemberTownRepository memberTownRepository;
    @BeforeEach
    void clean() {
        memberTownRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("근처동네 추가 테스트")
    void createMemberTownApiTest() throws Exception {
        // given
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        CreateMemberTown.Request request = CreateMemberTown.Request.builder()
                .townId(1L)
                .townName("서울")
                .latitude(33.319789)
                .longitude(126.774678)
                .build();

        String body = objectMapper.writeValueAsString(request);

        // when & then
        givenAuth(body,
                template.requestRestDocumentation(
                        "근처동네 추가",
                        getCreateMemberTownRequestFields(),
                        CreateMemberTown.Request.class.getName()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .post("/api/v1/members/towns")
                .then()
                .statusCode(HttpStatus.OK.value());

        List<MemberTown> memberTowns = memberTownRepository.getMemberTownFetchJoinByTownId(1L);
        assertThat(memberTowns.size()).isEqualTo(1);

        MemberTown memberTown = memberTowns.get(0);
        assertThat(memberTown.getMember().getEmail()).isEqualTo("test@gmail.com");
        assertThat(memberTown.getMember().getMemberDetail().getNickname()).isEqualTo("original");
        assertThat(passwordEncoder.matches("hello11@@nW", memberTown.getMember().getEncodedPassword()))
                .isTrue();
        assertThat(memberTown.getTownName()).isEqualTo("서울");
        assertThat(memberTown.getTownLocation().getLatitude()).isEqualTo(33.319789);
        assertThat(memberTown.getTownLocation().getLongitude()).isEqualTo(126.774678);
        assertThat(memberTown.getTown().getCode()).isEqualTo("5013032026");
    }

    @NotNull
    private List<FieldDescriptor> getCreateMemberTownRequestFields() {
        return Arrays.asList(
                fieldWithPath("townId").description("동네 id"),
                fieldWithPath("townName").description("동네 별칭"),
                fieldWithPath("latitude").description("위도"),
                fieldWithPath("longitude").description("경도")
        );
    }

    @NotNull
    private List<FieldDescriptor> getUpdateMemberPasswordRequestFields() {
        return List.of(fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"));
    }
}