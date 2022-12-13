package com.example.villagerservice.post.api;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.post.domain.*;
import com.example.villagerservice.post.dto.CreatePostComment;
import com.example.villagerservice.post.dto.CreatePost;
import com.example.villagerservice.post.dto.UpdatePost;
import com.example.villagerservice.post.exception.PostErrorCode;
import com.example.villagerservice.post.exception.PostException;
import com.example.villagerservice.post.service.PostCommentService;
import com.example.villagerservice.post.service.PostQueryService;
import com.example.villagerservice.post.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.RequestPartDescriptor;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;

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
        // given
        Category category = createCategory("이야기1");
        createToMember("post@gmail.com", "post");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("post@gmail.com", "hello11@@nW");
        CreatePost.Request createPost = CreatePost.Request.builder()
                .categoryId(category.getId())
                .title("제목입니다.")
                .contents("내용입니다.")
                .build();

        String body = objectMapper.writeValueAsString(createPost);

        // when & then
        givenAuthParts(
                "",
                template.requestRestPartsDocumentation(
                        "게시글 작성",
                        getCreatePostPartsRequestFields(),
                        CreatePost.Request.class.getName()))
                .multiPart("post", body, APPLICATION_JSON_VALUE)
                .multiPart("files", StringUtils.EMPTY)
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .post("/api/v1/posts")
                .then()
                .statusCode(HttpStatus.OK.value());

        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 변경 테스트")
    void updatePostApiTest() throws JsonProcessingException {
        // given
        Category category1 = createCategory("이야기1");
        Category category2 = createCategory("이야기2");
        Member member = createToMember("post@gmail.com", "post");

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("post@gmail.com", "hello11@@nW");

        Post post = new Post(member, category1, "제목입니다", "본문입니다.");
        postRepository.save(post);

        assertThat(postRepository.count()).isEqualTo(1);

        UpdatePost.Request updatePost = UpdatePost.Request.builder()
                .categoryId(category2.getId())
                .title("제목 변경되었습니다.")
                .contents("본문 변경되었습니다.")
                .build();
        String body = objectMapper.writeValueAsString(updatePost);

        // when & then
        givenAuth(body,
                template.requestRestDocumentation(
                        "게시글 변경",
                        getUpdatePostRequestFields(),
                        UpdatePost.Request.class.getName(),
                        getPostIdPathParameterFields()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .put("/api/v1/posts/{id}", post.getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        Post findPost = postRepository.getPostWithCategory(post.getId())
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        assertThat(findPost.getTitle()).isEqualTo("제목 변경되었습니다.");
        assertThat(findPost.getContents()).isEqualTo("본문 변경되었습니다.");
        assertThat(findPost.getCategory().getId()).isEqualTo(category2.getId());
        assertThat(findPost.getCategory().getName()).isEqualTo("이야기2");
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void deletePostApiTest() throws JsonProcessingException {
        // given
        Category category = createCategory("이야기");
        Member member = createToMember("post@gmail.com", "post");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("post@gmail.com", "hello11@@nW");
        Post post = new Post(member, category, "제목입니다", "본문입니다.");
        postRepository.save(post);

        assertThat(postRepository.count()).isEqualTo(1);
        assertThat(post.isDeleted()).isFalse();

        // when & then
        givenAuth("",
                template.requestRestDocumentation(
                        "게시글 삭제",
                        getPostIdPathParameterFields()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .delete("/api/v1/posts/{id}", post.getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        Post findPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        assertThat(findPost.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("게시글 댓글 추가 테스트")
    void createPostCommentApiTest() throws JsonProcessingException {
        // given
        Category category = createCategory("이야기");
        Member member = createToMember("post@gmail.com", "post");
        Post post = new Post(member, category, "제목입니다", "본문입니다.");
        postRepository.save(post);

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("post@gmail.com", "hello11@@nW");

        postCommentRepository.save(new PostComment(member, post, "댓글 기존1"));

        assertThat(postRepository.count()).isEqualTo(1);
        CreatePostComment.Request createPost = CreatePostComment.Request.builder()
                .comment("안녕하세요 댓글 2입니다.")
                .build();
        String body = objectMapper.writeValueAsString(createPost);

        // when & then
        givenAuth(
                body,
                template.requestRestDocumentation(
                        "게시글 댓글 작성",
                        getCreatePostCommentRequestFields(),
                        CreatePostComment.Request.class.getName(),
                        getPostIdPathParameterFields()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .post("/api/v1/posts/comments/{id}", post.getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        List<PostComment> comments = postCommentRepository.findByPost(post);
        assertThat(comments.size()).isEqualTo(2);
        assertThat(comments.get(0).getContents()).isEqualTo("댓글 기존1");
        assertThat(comments.get(1).getContents()).isEqualTo("안녕하세요 댓글 2입니다.");
    }

    @Test
    @DisplayName("게시글 댓글 삭제 테스트")
    void deleteCommentApiTest() throws JsonProcessingException {
        // given
        Category category = createCategory("이야기");
        Member member = createToMember("post@gmail.com", "post");
        Post post = new Post(member, category, "제목입니다", "본문입니다.");
        postRepository.save(post);

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("post@gmail.com", "hello11@@nW");
        postCommentRepository.save(new PostComment(member, post, "댓글 기존1"));
        PostComment comment2 = postCommentRepository.save(new PostComment(member, post, "댓글 기존2"));
        assertThat(postCommentRepository.count()).isEqualTo(2);

        // when & then
        givenAuth(
                "",
                template.requestRestDocumentation(
                        "게시글 댓글 삭제",
                        getDeleteCommentPathParameterFields()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .delete("/api/v1/posts/comments/{postId}/{commentId}", post.getId(), comment2.getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        List<PostComment> comments = postCommentRepository.findByPost(post);
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getContents()).isEqualTo("댓글 기존1");
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

    private Category createCategory(String name) {
        return categoryRepository.save(Category.builder()
                .name(name)
                .build());
    }

    @NotNull
    private List<RequestPartDescriptor> getCreatePostPartsRequestFields() {
        return Arrays.asList(
                partWithName("post").description("게시글 정보"),
                partWithName("files").description("이미지")
        );
    }

    @NotNull
    private List<FieldDescriptor> getUpdatePostRequestFields() {
        return Arrays.asList(
                fieldWithPath("categoryId").description("카테고리 id"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                fieldWithPath("contents").type(JsonFieldType.STRING).description("내용")
        );
    }

    @NotNull
    private List<FieldDescriptor> getCreatePostCommentRequestFields() {
        return List.of(fieldWithPath("comment").description("댓글"));
    }

    @NotNull
    private List<ParameterDescriptorWithType> getPostIdPathParameterFields() {
        return List.of(new ParameterDescriptorWithType("id").description("게시글 id"));
    }
    @NotNull
    private List<ParameterDescriptorWithType> getDeleteCommentPathParameterFields() {
        return List.of(
                new ParameterDescriptorWithType("postId").description("게시글 id"),
                new ParameterDescriptorWithType("commentId").description("댓글 id")
        );
    }
}