package com.example.villagerservice.postlike.api;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.example.document.BaseDocumentation;
import com.example.document.RestDocumentationTemplate;
import com.example.villagerservice.common.jwt.JwtTokenResponse;
import com.example.villagerservice.config.AuthConfig;
import com.example.villagerservice.config.WithMockCustomMember;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberTownRepository;
import com.example.villagerservice.post.domain.Category;
import com.example.villagerservice.post.domain.CategoryRepository;
import com.example.villagerservice.post.domain.Post;
import com.example.villagerservice.post.domain.PostRepository;
import com.example.villagerservice.postlike.domain.PostLike;
import com.example.villagerservice.postlike.domain.PostLikeRepository;
import com.example.villagerservice.postlike.service.PostLikeService;
import com.example.villagerservice.town.service.TownQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({AuthConfig.class})
class PostLikeApiControllerIntegratedTest extends BaseDocumentation  {

    @Autowired
    private PostLikeService postLikeService;
    @Autowired
    private MemberTownRepository memberTownRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;

    private final RestDocumentationTemplate template = new RestDocumentationTemplate("게시글 좋아요 api");

    @BeforeEach
    void clean() {
        postLikeRepository.deleteAll();
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 좋아요 테스트")
    void postLikeCheckApiTest() throws Exception {
        Member member = createToMember("test@gmail.com", "helloNi");
        JwtTokenResponse jwtTokenResponse = getJwtTokenResponse(member.getEmail(), "hello11@@nW");
        Category category = Category.builder().name("일반").build();
        categoryRepository.save(category);
        Post post = postRepository.save(new Post(member, category, "게시글제목", "타이틀!"));

        givenAuth("",
                template.requestRestDocumentation(
                        "게시글 좋아요",
                        getPostLikeCheckPathParameterFields()
                ))
                .when()
                .header(AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken())
                .post("/api/v1/postlike/{postId}", post.getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        List<PostLike> postLikes = postLikeRepository.findAll();
        assertThat(postLikes.size()).isEqualTo(1);
    }

    @NotNull
    private List<ParameterDescriptorWithType> getPostLikeCheckPathParameterFields() {
        return List.of(new ParameterDescriptorWithType("postId").description("게시글 id"));
    }
}