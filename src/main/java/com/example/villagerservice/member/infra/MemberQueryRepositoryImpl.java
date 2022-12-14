package com.example.villagerservice.member.infra;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberQueryRepository;
import com.example.villagerservice.member.domain.Tag;
import com.example.villagerservice.member.dto.MemberDetail;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.stream.Collectors;

import static com.example.villagerservice.follow.domain.QFollow.follow;
import static com.example.villagerservice.member.domain.QMember.member;
import static com.example.villagerservice.member.domain.QMemberDetail.memberDetail;
import static com.example.villagerservice.party.domain.QParty.party;
import static com.example.villagerservice.post.domain.QPost.post;
import static com.querydsl.core.types.ExpressionUtils.count;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepositoryImpl implements MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public MemberDetail.Response getMyPage(Long memberId) {
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .leftJoin(member.tagCollection.tags).fetchJoin()
                .join(member.memberDetail).fetchJoin()
                .where(member.id.eq(memberId))
                .fetchOne();

        MemberDetail.Response response = queryFactory
                .select(Projections.constructor(MemberDetail.Response.class,
                        ExpressionUtils.as(
                                JPAExpressions.select(count(party.id))
                                        .from(party)
                                        .where(party.member.eq(member)),
                                "partyCount"),
                        ExpressionUtils.as(
                                JPAExpressions.select(count(post.id))
                                        .from(post)
                                        .where(post.member.eq(member)),
                                "postCount"),
                        ExpressionUtils.as(
                                JPAExpressions.select(count(follow.id))
                                        .from(follow)
                                        .where(follow.fromMember.eq(member)),
                                "follow"),
                        ExpressionUtils.as(
                                JPAExpressions.select(count(follow.id))
                                        .from(follow)
                                        .where(follow.toMember.eq(member)),
                                "follower")
                ))
                .from(member)
                .innerJoin(member.memberDetail, memberDetail)
                .where(member.id.eq(memberId),
                        member.isDeleted.eq(false))
                .fetchOne();

        setMemberDetailResponse(findMember, response);

        return response;
    }

    @Override
    public boolean getFollowState(Long fromMemberId, Long toMemberId) {
        Integer fetchFirst = queryFactory
                .selectOne()
                .from(follow)
                .where(follow.fromMember.id.eq(fromMemberId),
                        follow.toMember.id.eq(toMemberId))
                .fetchFirst();
        return fetchFirst != null;
    }

    private void setMemberDetailResponse(Member findMember, MemberDetail.Response response) {
        if (findMember != null && response != null) {
            response.setMemberId(findMember.getId());
            response.setNickName(findMember.getMemberDetail().getNickname());
            response.setEmail(findMember.getEmail());
            response.setIntroduce(findMember.getMemberDetail().getIntroduce());
            response.setMannerPoint(findMember.getMemberDetail().getMannerPoint().getPoint());
            response.addTags(findMember.getTagCollection().getTags().stream().map(Tag::getName)
                    .collect(Collectors.toList()));
        }
    }
}
