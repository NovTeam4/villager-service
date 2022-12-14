package com.example.villagerservice.follow.service.impl;

import com.example.villagerservice.follow.domain.Follow;
import com.example.villagerservice.follow.domain.FollowRepository;
import com.example.villagerservice.follow.exception.FollowErrorCode;
import com.example.villagerservice.follow.exception.FollowException;
import com.example.villagerservice.follow.service.FollowService;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.villagerservice.follow.exception.FollowErrorCode.*;
import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Override
    public void following(Long fromMemberId, Long toMemberId) {
        Member fromMember = findMemberById(fromMemberId);
        Member toMember = findMemberById(toMemberId);

        followStatusValid(fromMember, toMember);
        followRepository.save(Follow.createFollow(fromMember, toMember));
    }

    @Override
    public void unFollowing(Long fromMemberId, Long toMemberId) {
        Member fromMember = findMemberById(fromMemberId);
        Member toMember = findMemberById(toMemberId);

        unFollowStatusValid(fromMember, toMember);
        Follow follow = followRepository.findByFromMemberAndToMember(fromMember, toMember)
                .orElseThrow(() -> new FollowException(FOLLOW_INTERNAL_ERROR));
        followRepository.delete(follow);
    }

    private void followStatusValid(Member fromMember, Member toMember) {
        if (followRepository.existsByFromMemberAndToMember(fromMember, toMember)) {
            throw new FollowException(FOLLOW_ALREADY_STATUS);
        }
    }

    private void unFollowStatusValid(Member fromMember, Member toMember) {
        if (!followRepository.existsByFromMemberAndToMember(fromMember, toMember)) {
            throw new FollowException(UNFOLLOW_ALREADY_STATUS);
        }
    }

    private Member findMemberById(Long fromMemberId) {
        return memberRepository.findById(fromMemberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }
}
