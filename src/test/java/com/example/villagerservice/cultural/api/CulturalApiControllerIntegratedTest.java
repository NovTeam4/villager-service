package com.example.villagerservice.cultural.api;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.cultural.dto.CulturalBannerDto;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.dto.CreateMember;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@ActiveProfiles({"town-test"})
@Import({AuthConfig.class})
class CulturalApiControllerIntegratedTest extends BaseDocumentation {

    private RestDocumentationTemplate template = new RestDocumentationTemplate("행사 API");
    
    @Test
    @DisplayName("행사 배너 목록")
    void getBannersApiTest() throws Exception {
        // when & then
        givenAuthPass("",
                template.responseRestDocumentation(
                        "행사 배너 목록",
                        getBannersResponseFields(),
                        CulturalBannerDto.Response.class.getName(),
                        getBannersPathParameterFields()))
                .when()
                .get("/api/v1/banners/{size}", 10)
                .then()
                .statusCode(HttpStatus.OK.value());
    }


    @NotNull
    private List<FieldDescriptor> getBannersResponseFields() {
        return Arrays.asList(
                fieldWithPath("totalCount").description("배너 개수"),
                fieldWithPath("banners").type(JsonFieldType.ARRAY).description("배너"),
                fieldWithPath("banners[].culturalId").description("행사 id"),
                fieldWithPath("banners[].codeName").description("코드명"),
                fieldWithPath("banners[].title").description("제목"),
                fieldWithPath("banners[].guName").description("구"),
                fieldWithPath("banners[].place").description("장소"),
                fieldWithPath("banners[].orgLink").description("url"),
                fieldWithPath("banners[].mainImage").description("이미지 경로"),
                fieldWithPath("banners[].startDate").description("시작일"),
                fieldWithPath("banners[].endDate").description("종료일"),
                fieldWithPath("banners[].date").description("행사 기간")
        );
    }

    @NotNull
    private List<ParameterDescriptorWithType> getBannersPathParameterFields() {
        return List.of(new ParameterDescriptorWithType("size").description("배너 개수"));
    }

}