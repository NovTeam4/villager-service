package com.example.villagerservice.post.api;

import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.post.domain.Category;
import com.example.villagerservice.post.domain.CategoryRepository;
import com.example.villagerservice.post.domain.PostCommentRepository;
import com.example.villagerservice.post.domain.PostRepository;
import com.example.villagerservice.post.dto.CreatePost;
import com.example.villagerservice.post.service.PostCommentService;
import com.example.villagerservice.post.service.PostQueryService;
import com.example.villagerservice.post.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.Arrays;
import java.util.List;

import static com.example.villagerservice.member.domain.Gender.MAN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@Import({AuthConfig.class})
class PostApiControllerIntegratedTest extends BaseDocumentation {
    private RestDocumentationTemplate template = new RestDocumentationTemplate("게시글 API");

    @Autowired
    private PostService postService;
    @Autowired
    private PostQueryService postQueryService;
    @Autowired
    private PostCommentService postCommentService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostCommentRepository postCommentRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void cleanAfterEach() {
        postCommentRepository.deleteAll();
        postRepository.deleteAll();
        categoryRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 생성 테스트")
    void createPostApiTest() throws JsonProcessingException {

    }

    @NotNull
    private List<FieldDescriptor> getCreatePostRequestFields() {
        return Arrays.asList(
                fieldWithPath("categoryId").description("카테고리 id"),
                fieldWithPath("title").description("제목"),
                fieldWithPath("contents").description("내용")
        );
    }

    private Category createCategory() {
        return categoryRepository.save(Category.builder()
                .name("이야기")
                .build());
    }

    @Test
    @DisplayName("게시글 변경 테스트")
    void updatePostApiTest() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("게시글 변경 테스트")
    void deletePostApiTest() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("게시글 목록 조회 테스트")
    void getPostListApiTest() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("게시글 상세 조회 테스트")
    void getPostApiTest() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("게시글 댓글 추가 테스트")
    void createPostCommentApiTest() {
        // given

        // when

        // then
    }
}