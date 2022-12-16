package com.example.villagerservice.member.service.impl;

import com.example.villagerservice.follow.domain.FollowRepository;
import com.example.villagerservice.member.domain.MemberQueryRepository;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.dto.MemberDetail;
import com.example.villagerservice.member.exception.MemberErrorCode;
import com.example.villagerservice.member.exception.MemberException;
import com.example.villagerservice.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberQueryRepository memberQueryRepository;

    @Override
    public MemberDetail.Response getMyPage(Long memberId) {
        MemberDetail.Response response = memberQueryRepository.getMyPage(memberId);
        if(response == null) {
            throw new MemberException(MEMBER_NOT_FOUND);
        }
        return response;
    }

    @Override
    public MemberDetail.Response getOtherMyPage(Long memberId, Long otherMemberId) {
        MemberDetail.Response response = memberQueryRepository.getMyPage(otherMemberId);
        if(response == null) {
            throw new MemberException(MEMBER_NOT_FOUND);
        }
        boolean state = memberQueryRepository.getFollowState(memberId, otherMemberId);
        response.setFollowState(state);
        return response;
    }
}
