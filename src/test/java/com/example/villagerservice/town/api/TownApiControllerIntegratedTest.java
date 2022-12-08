package com.example.villagerservice.town.api;

import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.dto.LoginMember;
import com.example.villagerservice.member.dto.UpdateMemberInfo;
import com.example.villagerservice.town.dto.TownList;
import com.example.villagerservice.town.dto.TownListDetail;
import com.example.villagerservice.town.service.TownQueryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"town-test"})
@Import({AuthConfig.class})
class TownApiControllerIntegratedTest extends BaseDocumentation {

    @Autowired
    private TownQueryService townQueryService;
    private RestDocumentationTemplate template = new RestDocumentationTemplate("근처동네 API");

    @Test
    @DisplayName("근처동네 위치 정보로 조회 api 테스트")
    void getTownListWithLocationApiTest() throws JsonProcessingException {
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        TownList.LocationRequest request = TownList.LocationRequest.builder()
                .latitude(37.584009)
                .longitude(126.970626)
                .build();

        String body = objectMapper.writeValueAsString(request);

        Response response = givenAuth(body,
                template.responseRestDocumentation(
                        "근처동네 위치로 조회",
                        getTownListWithLocationResponseFields(),
                        TownList.Response.class.getName()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .get("/api/v1/towns/location");

        TownList.Response townListResponse = objectMapper.readValue(response.asString(), TownList.Response.class);

        response
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("totalCount", Matchers.equalTo(townListResponse.getTotalCount()));
    }

    @Test
    @DisplayName("근처동네 위치 정보로 조회 api 테스트")
    void getTownListWithNameApiTest() throws JsonProcessingException {
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse();
        TownList.NameRequest request = TownList.NameRequest.builder()
                .name("여의동")
                .build();

        String body = objectMapper.writeValueAsString(request);

        Response response = givenAuth(body,
                template.responseRestDocumentation(
                        "근처동네 이름으로 조회",
                        getTownListWithLocationResponseFields(),
                        TownList.Response.class.getName()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .get("/api/v1/towns/name");

        TownList.Response townListResponse = objectMapper.readValue(response.asString(), TownList.Response.class);

        response
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("totalCount", Matchers.equalTo(townListResponse.getTotalCount()));
    }

    @NotNull
    private List<FieldDescriptor> getTownListWithLocationResponseFields() {
        return Arrays.asList(
                fieldWithPath("totalCount").description("전체수"),
                fieldWithPath("towns").type(JsonFieldType.ARRAY).description("근처동네 목록"),
                fieldWithPath("towns[].name").type(JsonFieldType.STRING).description("동네이름"),
                fieldWithPath("towns[].code").description("코드"),
                fieldWithPath("towns[].latitude").description("위도"),
                fieldWithPath("towns[].longitude").description("경도")
        );
    }
}

