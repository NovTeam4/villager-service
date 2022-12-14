package com.example.villagerservice.post.api;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.post.domain.*;
import com.example.villagerservice.post.dto.*;
import com.example.villagerservice.post.exception.PostErrorCode;
import com.example.villagerservice.post.exception.PostException;
import com.example.villagerservice.post.service.PostCommentService;
import com.example.villagerservice.post.service.PostQueryService;
import com.example.villagerservice.post.service.PostService;
import com.example.villagerservice.postlike.domain.PostLike;
import com.example.villagerservice.postlike.domain.PostLikeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
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
    private RestDocumentationTemplate template = new RestDocumentationTemplate("????????? API");

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
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Value("${aws.s3.base-url}")
    private String awsS3baseUrl;

    @AfterEach
    void cleanAfterEach() {
        postCommentRepository.deleteAll();
        postLikeRepository.deleteAll();
        postRepository.deleteAll();
        categoryRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("????????? ?????? ?????????")
    void createPostApiTest() throws JsonProcessingException {
        // given
        Category category = createCategory("?????????1");
        createToMember("post@gmail.com", "post");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("post@gmail.com", "hello11@@nW");
        CreatePost.Request createPost = CreatePost.Request.builder()
                .categoryId(category.getId())
                .title("???????????????.")
                .contents("???????????????.")
                .build();

        String body = objectMapper.writeValueAsString(createPost);

        // when & then
        givenAuthParts(
                "",
                template.requestRestPartsDocumentation(
                        "????????? ??????",
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
    @DisplayName("????????? ?????? ?????????")
    void updatePostApiTest() throws JsonProcessingException {
        // given
        Category category1 = createCategory("?????????1");
        Category category2 = createCategory("?????????2");
        Member member = createToMember("post@gmail.com", "post");

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("post@gmail.com", "hello11@@nW");

        Post post = new Post(member, category1, "???????????????", "???????????????.");
        postRepository.save(post);

        assertThat(postRepository.count()).isEqualTo(1);

        UpdatePost.Request updatePost = UpdatePost.Request.builder()
                .categoryId(category2.getId())
                .title("?????? ?????????????????????.")
                .contents("?????? ?????????????????????.")
                .build();
        String body = objectMapper.writeValueAsString(updatePost);

        // when & then
        givenAuth(body,
                template.requestRestDocumentation(
                        "????????? ??????",
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
        assertThat(findPost.getTitle()).isEqualTo("?????? ?????????????????????.");
        assertThat(findPost.getContents()).isEqualTo("?????? ?????????????????????.");
        assertThat(findPost.getCategory().getId()).isEqualTo(category2.getId());
        assertThat(findPost.getCategory().getName()).isEqualTo("?????????2");
    }

    @Test
    @DisplayName("????????? ?????? ?????????")
    void deletePostApiTest() throws JsonProcessingException {
        // given
        Category category = createCategory("?????????");
        Member member = createToMember("post@gmail.com", "post");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("post@gmail.com", "hello11@@nW");
        Post post = new Post(member, category, "???????????????", "???????????????.");
        postRepository.save(post);

        assertThat(postRepository.count()).isEqualTo(1);
        assertThat(post.isDeleted()).isFalse();

        // when & then
        givenAuth("",
                template.requestRestDocumentation(
                        "????????? ??????",
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
    @DisplayName("????????? ?????? ?????? ?????????")
    void createPostCommentApiTest() throws JsonProcessingException {
        // given
        Category category = createCategory("?????????");
        Member member = createToMember("post@gmail.com", "post");
        Post post = new Post(member, category, "???????????????", "???????????????.");
        postRepository.save(post);

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("post@gmail.com", "hello11@@nW");

        postCommentRepository.save(new PostComment(member, post, "?????? ??????1"));

        assertThat(postRepository.count()).isEqualTo(1);
        CreatePostComment.Request createPost = CreatePostComment.Request.builder()
                .comment("??????????????? ?????? 2?????????.")
                .build();
        String body = objectMapper.writeValueAsString(createPost);

        // when & then
        givenAuth(
                body,
                template.requestRestDocumentation(
                        "????????? ?????? ??????",
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
        assertThat(comments.get(0).getContents()).isEqualTo("?????? ??????1");
        assertThat(comments.get(1).getContents()).isEqualTo("??????????????? ?????? 2?????????.");
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    void updatePostCommentApiTest() throws JsonProcessingException {
        // given
        Category category = createCategory("?????????");
        Member member = createToMember("post@gmail.com", "post");
        Post post = new Post(member, category, "???????????????", "???????????????.");
        postRepository.save(post);

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("post@gmail.com", "hello11@@nW");
        PostComment comment = postCommentRepository.save(new PostComment(member, post, "?????? ??????2"));
        assertThat(postCommentRepository.count()).isEqualTo(1);

        UpdatePostComment.Request updatePost = UpdatePostComment.Request.builder()
                .comment("??????????????? ?????? 2?????????.")
                .build();
        String body = objectMapper.writeValueAsString(updatePost);

        // when & then
        givenAuth(
                body,
                template.requestRestDocumentation(
                        "????????? ?????? ??????",
                        getCreatePostCommentRequestFields(),
                        UpdatePostComment.Request.class.getName(),
                        getCommentPathParameterFields()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .put("/api/v1/posts/comments/{postId}/{commentId}", post.getId(), comment.getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        List<PostComment> comments = postCommentRepository.findByPost(post);
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getContents()).isEqualTo("??????????????? ?????? 2?????????.");
    }
    
    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    void deleteCommentApiTest() throws JsonProcessingException {
        // given
        Category category = createCategory("?????????");
        Member member = createToMember("post@gmail.com", "post");
        Post post = new Post(member, category, "???????????????", "???????????????.");
        postRepository.save(post);

        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("post@gmail.com", "hello11@@nW");
        postCommentRepository.save(new PostComment(member, post, "?????? ??????1"));
        PostComment comment2 = postCommentRepository.save(new PostComment(member, post, "?????? ??????2"));
        assertThat(postCommentRepository.count()).isEqualTo(2);

        // when & then
        givenAuth(
                "",
                template.requestRestDocumentation(
                        "????????? ?????? ??????",
                        getCommentPathParameterFields()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .delete("/api/v1/posts/comments/{postId}/{commentId}", post.getId(), comment2.getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        List<PostComment> comments = postCommentRepository.findByPost(post);
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getContents()).isEqualTo("?????? ??????1");
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    void getPostListApiTest() {
        // given
        Category category = createCategory("?????????");
        Member member = createToMember("post@gmail.com", "post");

        Post post = new Post(member, category, "???????????????", "???????????????.");
        postRepository.save(post);

        assertThat(postRepository.count()).isEqualTo(1);

        // when & then
        Response response = givenAuthPass("",
                template.allRestDocumentation(
                        "????????? ?????? ??????",
                        getPostListRequestParameterFields(),
                        ListPostSearchCond.class.getName(),
                        getPostListResponseFields(),
                        Page.class.getName()))
                .when()
                .get("/api/v1/posts?size=10&page=0&title=??????&categoryId=" + category.getId());

        response
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", Matchers.equalTo(1))
                .body("content[0].nickName", Matchers.equalTo("post"))
                .body("content[0].viewCount", Matchers.equalTo(0))
                .body("content[0].likeCount", Matchers.equalTo(0))
                .body("content[0].title", Matchers.equalTo("???????????????"))
                .body("content[0].categoryName", Matchers.equalTo("?????????"));
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    void getPostApiTest() {
        // given
        Category category = createCategory("?????????");
        Member member = createToMember("post@gmail.com", "post");

        Post post = new Post(member, category, "???????????????.", "???????????????.");
        post.addImages(PostImage.createPostImage(12345L, "aaa.png"));
        post.addImages(PostImage.createPostImage(23457L, "bbb.png"));
        postRepository.save(post);
        assertThat(postRepository.count()).isEqualTo(1);

        postCommentRepository.save(new PostComment(member, post, "??????1"));
        postCommentRepository.save(new PostComment(member, post, "??????2"));
        assertThat(postCommentRepository.count()).isEqualTo(2);

        postLikeRepository.save(PostLike.createPost(member, post));
        assertThat(postLikeRepository.count()).isEqualTo(1);

        // when & then
        Response response = givenAuthPass("",
                template.responseRestDocumentation(
                        "????????? ?????? ??????",
                        getPostDetailResponseFields(),
                        PostItemDetail.class.getName(),
                        getPostDetailPathParameterFields()))
                .when()
                .get("/api/v1/posts/{postId}", post.getId());

        response
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("postId", Matchers.equalTo(post.getId().intValue()))
                .body("categoryId", Matchers.equalTo(category.getId().intValue()))
                .body("nickName", Matchers.equalTo("post"))
                .body("viewCount", Matchers.equalTo(1))
                .body("likeCount", Matchers.equalTo(1))
                .body("title", Matchers.equalTo("???????????????."))
                .body("contents", Matchers.equalTo("???????????????."))
                .body("categoryName", Matchers.equalTo("?????????"))
                .body("images.size()", Matchers.equalTo(2))
                .body("images[0].path", Matchers.equalTo(awsS3baseUrl.concat("aaa.png")))
                .body("images[0].size", Matchers.equalTo(12345))
                .body("images[1].path", Matchers.equalTo(awsS3baseUrl.concat("bbb.png")))
                .body("images[1].size", Matchers.equalTo(23457))
                .body("comments.size()", Matchers.equalTo(2))
                .body("comments[0].memberId", Matchers.equalTo(member.getId().intValue()))
                .body("comments[0].nickname", Matchers.equalTo("post"))
                .body("comments[1].memberId", Matchers.equalTo(member.getId().intValue()))
                .body("comments[1].nickname", Matchers.equalTo("post"))
                ;
    }

    @Test
    @DisplayName("????????? ???????????? ?????? ??????")
    void getPostCategoryToList() throws JsonProcessingException {
        // given
        Category category1 = createCategory("?????????");
        Category category2 = createCategory("?????????2");
        Member member = createToMember("post@gmail.com", "post");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse("post@gmail.com", "hello11@@nW");

        assertThat(categoryRepository.count()).isEqualTo(2);

        // when & then
        Response response = givenAuth("",
                template.responseRestDocumentation(
                        "????????? ???????????? ??????",
                        getCategoryListResponseFields(),
                        CategoryDto.Response.class.getName()))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .get("/api/v1/posts/category");

        response
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].name", Matchers.equalTo("?????????"))
                .body("[1].name", Matchers.equalTo("?????????2"))
        ;
    }

    private Category createCategory(String name) {
        return categoryRepository.save(Category.builder()
                .name(name)
                .build());
    }

    @NotNull
    private List<ParameterDescriptorWithType> getPostListRequestParameterFields() {
        return Arrays.asList(
                new ParameterDescriptorWithType("categoryId").description("???????????? id"),
                new ParameterDescriptorWithType("title").description("??????"),
                new ParameterDescriptorWithType("page").description("????????? ??????"),
                new ParameterDescriptorWithType("size").description("?????? ??????")
        );
    }

    @NotNull
    private List<FieldDescriptor> getPostListResponseFields() {
        List<FieldDescriptor> fieldDescriptors = Arrays.asList(
                fieldWithPath("content").type(JsonFieldType.ARRAY).description("?????????"),
                fieldWithPath("content[].postId").description("????????? id"),
                fieldWithPath("content[].categoryId").description("???????????? id"),
                fieldWithPath("content[].nickName").description("?????????"),
                fieldWithPath("content[].viewCount").description("?????????"),
                fieldWithPath("content[].likeCount").description("????????? ???"),
                fieldWithPath("content[].title").description("??????"),
                fieldWithPath("content[].categoryName").description("???????????????"),
                fieldWithPath("content[].createAt").description("?????????"),
                fieldWithPath("content[].nearCreateAt").description("?????????2")
        );
        return getContentsConcatPageResponseFields(fieldDescriptors);
    }

    @NotNull
    private List<FieldDescriptor> getCategoryListResponseFields() {
        return Arrays.asList(
                fieldWithPath("[].categoryId").description("???????????? id"),
                fieldWithPath("[].name").description("???????????????")
        );
    }

    @NotNull
    private List<FieldDescriptor> getPostDetailResponseFields() {
        return Arrays.asList(
                fieldWithPath("postId").description("????????? id"),
                fieldWithPath("categoryId").description("????????? id"),
                fieldWithPath("nickName").description("????????? id"),
                fieldWithPath("viewCount").description("????????? id"),
                fieldWithPath("likeCount").description("????????? id"),
                fieldWithPath("title").description("????????? id"),
                fieldWithPath("contents").description("????????? id"),
                fieldWithPath("categoryName").description("????????? id"),
                fieldWithPath("createAt").description("????????? id"),
                fieldWithPath("nearCreateAt").description("????????? id"),
                fieldWithPath("images").type(JsonFieldType.ARRAY).description("????????? ??????"),
                fieldWithPath("images[].imageId").description("????????? id"),
                fieldWithPath("images[].path").description("??????"),
                fieldWithPath("images[].size").description("?????????"),
                fieldWithPath("comments").type(JsonFieldType.ARRAY).description("?????? ??????"),
                fieldWithPath("comments[].commentId").description("?????? id"),
                fieldWithPath("comments[].memberId").description("?????? ????????? id"),
                fieldWithPath("comments[].nickname").description("?????? ????????? ?????????"),
                fieldWithPath("comments[].comment").description("?????? ??????"),
                fieldWithPath("comments[].createdAt").description("?????? ????????????")
        );
    }
    @NotNull
    private List<RequestPartDescriptor> getCreatePostPartsRequestFields() {
        return Arrays.asList(
                partWithName("post").description("????????? ??????"),
                partWithName("files").description("?????????")
        );
    }

    @NotNull
    private List<FieldDescriptor> getUpdatePostRequestFields() {
        return Arrays.asList(
                fieldWithPath("categoryId").description("???????????? id"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("??????"),
                fieldWithPath("contents").type(JsonFieldType.STRING).description("??????")
        );
    }

    @NotNull
    private List<FieldDescriptor> getCreatePostCommentRequestFields() {
        return List.of(fieldWithPath("comment").description("??????"));
    }
    @NotNull
    private List<ParameterDescriptorWithType> getPostIdPathParameterFields() {
        return List.of(new ParameterDescriptorWithType("id").description("????????? id"));
    }

    @NotNull
    private List<ParameterDescriptorWithType> getPostDetailPathParameterFields() {
        return List.of(new ParameterDescriptorWithType("postId").description("????????? id"));
    }

    @NotNull
    private List<ParameterDescriptorWithType> getCommentPathParameterFields() {
        return List.of(
                new ParameterDescriptorWithType("postId").description("????????? id"),
                new ParameterDescriptorWithType("commentId").description("?????? id")
        );
    }
}