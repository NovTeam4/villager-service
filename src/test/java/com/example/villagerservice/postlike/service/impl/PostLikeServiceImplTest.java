package com.example.villagerservice.postlike.service.impl;

import com.example.villagerservice.member.domain.Gender;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.exception.MemberException;
import com.example.villagerservice.post.domain.Post;
import com.example.villagerservice.post.domain.PostRepository;
import com.example.villagerservice.post.exception.PostException;
import com.example.villagerservice.postlike.domain.PostLike;
import com.example.villagerservice.postlike.domain.PostLikeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.example.villagerservice.post.exception.PostErrorCode.POST_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostLikeServiceImplTest {
    @Mock
    private PostLikeRepository postLikeRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private PostLikeServiceImpl postLikeService;

    @Test
    @DisplayName("게시글 좋아요 테스트 시 회원이 존재하지 않을 경우 테스트")
    void postLikeCheckNotFoundMemberTest() {
        // given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        MemberException memberException
                = assertThrows(MemberException.class, () -> postLikeService.postLikeCheck(1L, anyLong()));


        // then
        verify(memberRepository, times(1)).findById(anyLong());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_NOT_FOUND);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("게시글 좋아요 테스트 시 게시글이 존재하지 않을 경우 테스트")
    void postLikeCheckNotFoundPostTest() {
        // given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder().build()));
        given(postRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        PostException postException
                = assertThrows(PostException.class, () -> postLikeService.postLikeCheck(1L, anyLong()));


        // then
        verify(memberRepository, times(1)).findById(anyLong());
        verify(postRepository, times(1)).findById(anyLong());
        assertThat(postException.getPostErrorCode()).isEqualTo(POST_NOT_FOUND);
        assertThat(postException.getErrorCode()).isEqualTo(POST_NOT_FOUND.getErrorCode());
        assertThat(postException.getErrorMessage()).isEqualTo(POST_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("게시글 좋아요 테스트")
    void postLikeTest() {
        // given
        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("nickname")
                .gender(Gender.MAN)
                .build();

        Post post = new Post(member, null, "title", "contents");
        PostLike postLike = PostLike.createPost(member, post);

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(post));
        ArgumentCaptor<PostLike> captor = ArgumentCaptor.forClass(PostLike.class);

        // when
        postLikeService.postLikeCheck(1L, 1L);


        // then
        verify(memberRepository, times(1)).findById(anyLong());
        verify(postRepository, times(1)).findById(anyLong());
        verify(postLikeRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getMember().getMemberDetail().getNickname())
                .isEqualTo("nickname");
        assertThat(captor.getValue().getMember().getEmail())
                .isEqualTo("test@gmail.com");
        assertThat(captor.getValue().getPost().getTitle())
                .isEqualTo("title");
        assertThat(captor.getValue().getPost().getContents())
                .isEqualTo("contents");
    }

}