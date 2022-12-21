package com.example.villagerservice.commenttemplates.api;

import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.commenttemplates.domain.CommentTemplate;
import com.example.villagerservice.commenttemplates.domain.CommentTemplateRepository;
import com.example.villagerservice.commenttemplates.dto.CommentTemplateDto;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.post.dto.CategoryDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
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
public class CommentTemplateControllerIntegratedTest extends BaseDocumentation {
    private RestDocumentationTemplate template = new RestDocumentationTemplate("후기 템플릿 API");

    @Autowired
    private CommentTemplateRepository commentTemplateRepository;

    @Test
    @DisplayName("후기 템플릿 목록 조회")
    void getPostCategoryToList() throws JsonProcessingException {
        // given
        commentTemplateRepository.save(new CommentTemplate("템플릿 후기1"));
        commentTemplateRepository.save(new CommentTemplate("템플릿 후기2"));

        assertThat(commentTemplateRepository.count()).isEqualTo(2);

        Member member = createToMember("post@gmail.com", "post");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("post@gmail.com", "hello11@@nW");


        // when & then
        Response response = givenAuth("",
                template.responseRestDocumentation(
                        "후기 템플릿 조회",
                        getCommentTemplateResponseFields(),
                        CommentTemplateDto.Response.class.getName()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .get("/api/v1/comments/template");

        response
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("totalCount", Matchers.equalTo(2))
        ;
    }

    @NotNull
    private List<FieldDescriptor> getCommentTemplateResponseFields() {
        return Arrays.asList(
                fieldWithPath("totalCount").description("후기 템플릿 수"),
                fieldWithPath("templates").type(JsonFieldType.ARRAY).description("후기 템플릿 목록"),
                fieldWithPath("templates[].commentTemplateId").description("후기 템플릿 id"),
                fieldWithPath("templates[].commentTemplateContent").description("후기 템플릿")
        );
    }
}
