package com.example.villagerservice.post.service.impl;

import com.example.villagerservice.comment.excepiton.CommentException;
import com.example.villagerservice.common.exception.CommonErrorCode;
import com.example.villagerservice.member.domain.Gender;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.exception.MemberException;
import com.example.villagerservice.post.domain.Post;
import com.example.villagerservice.post.domain.PostComment;
import com.example.villagerservice.post.domain.PostCommentRepository;
import com.example.villagerservice.post.domain.PostRepository;
import com.example.villagerservice.post.dto.CreatePostComment;
import com.example.villagerservice.post.dto.UpdatePostComment;
import com.example.villagerservice.post.exception.PostException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

import static com.example.villagerservice.comment.excepiton.CommentErrorCode.COMMENT_POST_NOT_FOUND;
import static com.example.villagerservice.common.exception.CommonErrorCode.DATA_INVALID_ERROR;
import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.example.villagerservice.post.exception.PostErrorCode.COMMENT_ROLE_NOT;
import static com.example.villagerservice.post.exception.PostErrorCode.POST_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostCommentServiceImplTest {


    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PostRepository postRepository;

    @Mock
    private PostCommentRepository postCommentRepository;
    @InjectMocks
    private PostCommentServiceImpl postCommentServiceimpl;

    /**
     *    deletePostComment   댓글 달기 테스트
     */


    @Test
    @DisplayName("댓글 작성시 (게시글)이 존재하지 않을 경우 테스트 ")
    void createPostCommentNotFoundPost(){


        //givin
      given(postRepository.findById(anyLong()))
              .willReturn(Optional.empty());  // 아무것도 xx


        //when
        PostException postException = assertThrows(PostException.class,
                ()-> postCommentServiceimpl.createPostComment(1L,1L,any()));

      //then
        assertThat(postException.getPostErrorCode()).isEqualTo(POST_NOT_FOUND);
        assertThat(postException.getErrorCode()).isEqualTo(POST_NOT_FOUND.getErrorCode());
        assertThat(postException.getErrorMessage()).isEqualTo(POST_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("댓글 작성시 (유저)가 존재하지 않을 경우 테스트")
    void createPostCommentNotFoundUser(){

        Member host = Member.builder()
                .email("host@gmail.com")
                .nickname("host")
                .gender(Gender.MAN)
                .build();


        Post post = Post.builder()
                .id(1L)
                .member(host)
                .build();


        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(post));

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when

        MemberException memberException = assertThrows(MemberException.class,
                () -> postCommentServiceimpl.createPostComment(1L,1L,any()));

        //then
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_NOT_FOUND);


    }


    @Test
    @DisplayName("댓글 생성시 (댓글에 한글씨라도 안썻을) 경우테스트")
    void createPostCommentNotInput(){
        // given
        Member user = Member.builder()
                .email("aaa@naver.com")
                .nickname("user")
                .build();

        Post post = Post.builder()
                .title("제목")
                .contents("내용")
                .member(user)
                .build();

        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(post));

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(user));

        CreatePostComment.Request comment = CreatePostComment.Request.builder()
                .comment("")
                .build();
        //when
        MethodArgumentNotValidException NotValidException = assertThrows(
                MethodArgumentNotValidException.class, () -> postCommentServiceimpl.createPostComment(1L, 1L, comment));


        //then


        assertThat(NotValidException).isEqualTo(DATA_INVALID_ERROR.getErrorMessage());

    }


    @Test
    @DisplayName("댓글 작성 정상테스트")
    void getCommentNotFoundTest() {

        //given

        Member host = Member.builder()
                .email("host@gmail.com")
                .nickname("host")
                .gender(Gender.MAN)
                .build();

        Post post = Post.builder()
                .id(1L)
                .member(host)
                .build();

        CreatePostComment.Request comment1 = CreatePostComment.Request.builder()
                .comment("테스트댓글")
                .build();


        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(post));

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(host));

        // when
        ArgumentCaptor<PostComment> captor = ArgumentCaptor.forClass(PostComment.class);  // 캡터를 생성하는부분
        postCommentServiceimpl.createPostComment(1L, 1L, comment1);


        // then
        verify(postRepository,times(1)).findById(anyLong());
        verify(memberRepository,times(1)).findById(anyLong());
        verify(postCommentRepository, times(1)).save(captor.capture());

        assertThat(captor.getValue().getContents()).isEqualTo("테스트댓글");



    }


    @Test
    @DisplayName("댓글 작성저장테스트")
    void  savePostCommentTest() {
        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of( Post.builder()
                        .id(1L)
                        .build()));

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of( Member.builder()
                                .email("aaa")
                        .build()));
        // when
        ArgumentCaptor<PostComment> captor = ArgumentCaptor.forClass(PostComment.class);
        postCommentServiceimpl.createPostComment(1L, 1L, CreatePostComment.Request.builder().comment("테스트댓글").build());


        // then
        verify(postRepository,times(1)).findById(anyLong());
        verify(memberRepository,times(1)).findById(anyLong());
        verify(postCommentRepository, times(1)).save(captor.capture());

        assertThat(captor.getValue().getContents()).isEqualTo("테스트댓글");
    }


    @Test
    @DisplayName("댓글 삭제시 게시글이 존재하지않는 경우의 테스트")
    void findNotPostComment(){

        //given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());
        //when

        //then
        PostException postException = assertThrows(PostException.class, () -> postCommentServiceimpl.createPostComment(1L, 1L, any()));
        //then
        assertThat(postException.getPostErrorCode()).isEqualTo(POST_NOT_FOUND);
        assertThat(postException.getErrorCode()).isEqualTo(POST_NOT_FOUND.getErrorCode());
        assertThat(postException.getErrorMessage()).isEqualTo(POST_NOT_FOUND.getErrorMessage());

    }

    /**
     *    deletePostComment   댓글삭제 테스트
     */

    @Test
    @DisplayName("댓글 삭제시 3개중 하나라도 파라미터가 일치하지 않는  테스트  ")
    void deletePostCommentParamNotFind(){

        //given

        Member member = Member.builder().email("aaaa@naver.com")
                .build();

        Post post = Post.builder()
                .title("data")
                .build();


        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(postCommentRepository.findByIdAndPostAndMember(1L,post,member)).willReturn(Optional.empty());

       //when

        PostException postException = assertThrows(PostException.class, () -> postCommentServiceimpl.deletePostComment(1L, 1L, 1L));
        //then

        assertThat(postException.getPostErrorCode()).isEqualTo(COMMENT_ROLE_NOT);

    }


    @Test
    @DisplayName("댓글 삭제 성공 테스트 ")
    void deletePostCommentSuccessTest(){

        //given
        Member member = Member.builder()
                .email("rudtjq1234@naver.com")
                .nickname("경섭")
                .gender(Gender.MAN)
                .build();
        Post post = Post.builder()
                .id(1L)
                .contents("내용블라블라")
                .member(member)
                .build();
//        postCommentServiceimpl.createPostComment(1L, 1L, CreatePostComment.Request.builder().comment("댓글입니다.").build());
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(postCommentRepository.findByIdAndPostAndMember(anyLong(),any(),any()))
                .willReturn(Optional.of(PostComment.builder()
                        .build()));

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        //when

        postCommentServiceimpl.deletePostComment(1L,1L,1L);

        //then
        verify(memberRepository,times(1)).findById(anyLong());
        verify(postRepository,times(1)).findById(anyLong());
        verify(postCommentRepository,times(1)).findByIdAndPostAndMember(anyLong(),any(),any());

        verify(postCommentRepository,times(1)).deleteById(captor.capture());

//        assertThat(captor.getValue().getContents()).isEqualTo("댓글입니다.");

        assertThat(captor.getValue()).isEqualTo(1L);

    }

    @Test
    @DisplayName("댓글 수정시 회원이 없을 경우 테스트")
    void updatePostCommentNotFoundMemberTest(){

        //given
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        UpdatePostComment.Request req = UpdatePostComment.Request.builder()
                .comment("변경됨")
                .build();
        // when
        MemberException memberException = assertThrows(MemberException.class, () ->
                postCommentServiceimpl.updatePostComment(1L, 1L, 1L,req));
        // then
        verify(memberRepository,times(1)).findById(anyLong());
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("게시글 댓글 수정 성공테스트 ")
    void SuccessTest(){
        //given
        Member member = Member.builder()
                .email("rudtjq1234@naver.com")
                .nickname("경섭")
                .gender(Gender.MAN)
                .build();

        Post post = Post.builder()
                .contents("내용블라블라")
                .member(member)
                .build();

        PostComment postComment = PostComment.builder().
                 contents("변경전")
                .member(member)
                .post(post)
                .build();

        PostComment  mockPostComment = spy(postComment);

        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(postCommentRepository.findByIdAndPostAndMember(1L,post,member))
                .willReturn(Optional.of(mockPostComment));

        UpdatePostComment.Request req = UpdatePostComment.Request.builder()
                .comment("변경됨")
                .build();
        //when

        postCommentServiceimpl.updatePostComment(1L,1L,1L, req);
        verify(mockPostComment,times(1)).updatePostComment(any(),any(),anyString());
        assertThat("변경됨").isEqualTo(mockPostComment.getContents());


    }













}
