package com.example.villagerservice.postlike.service.impl;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.exception.MemberErrorCode;
import com.example.villagerservice.member.exception.MemberException;
import com.example.villagerservice.post.domain.Post;
import com.example.villagerservice.post.domain.PostRepository;
import com.example.villagerservice.post.exception.PostErrorCode;
import com.example.villagerservice.post.exception.PostException;
import com.example.villagerservice.postlike.domain.PostLike;
import com.example.villagerservice.postlike.domain.PostLikeRepository;
import com.example.villagerservice.postlike.exception.PostLikeErrorCode;
import com.example.villagerservice.postlike.exception.PostLikeException;
import com.example.villagerservice.postlike.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.example.villagerservice.post.exception.PostErrorCode.POST_NOT_FOUND;
import static com.example.villagerservice.postlike.exception.PostLikeErrorCode.POST_LIKE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    public void postLikeCheck(Long memberId, Long postId) {
        Member member = findMemberById(memberId);
        Post post = findPostById(postId);

        PostLike postLike = PostLike.createPost(member, post);
        postLikeRepository.save(postLike);
    }

    @Override
    public void postLikeUnCheck(Long memberId, Long postId) {
        Member member = findMemberById(memberId);
        Post post = findPostById(postId);

        PostLike postLike = postLikeRepository.findByMemberAndPost(member, post)
                .orElseThrow(() -> new PostLikeException(POST_LIKE_NOT_FOUND));
        postLikeRepository.delete(postLike);
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }
}
