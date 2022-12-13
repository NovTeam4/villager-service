package com.example.villagerservice.post.service.impl;

import com.example.villagerservice.post.domain.PostComment;
import com.example.villagerservice.post.domain.PostCommentRepository;
import com.example.villagerservice.post.dto.CommentPost;
import com.example.villagerservice.post.service.PostCommentService;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.exception.MemberException;
import com.example.villagerservice.post.domain.Post;
import com.example.villagerservice.post.domain.PostRepository;
import com.example.villagerservice.post.exception.PostException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.example.villagerservice.post.exception.PostErrorCode.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor

public class PostCommentServiceImpl implements PostCommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;

    @Override
    public void createComment(Long memberId , Long postId, CommentPost.Request request) {
        Post post = findByPostId(postId);
        Member member = findByMemberId(memberId);
        PostComment postComment = new PostComment(member,post,request.getContents());

        postCommentRepository.save(postComment);
    }

    private Post findByPostId(Long postId){
        return postRepository.findById(postId).orElseThrow(()-> new PostException(POST_NOT_FOUND)); // 댓글작성할 게시글찾아서
    }

    private Member findByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }
}
