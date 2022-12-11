package com.example.villagerservice.follow.infra;

import com.example.villagerservice.follow.domain.FollowQueryRepository;
import com.example.villagerservice.follow.dto.FollowList;
import com.example.villagerservice.follow.dto.FollowListItem;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.villagerservice.follow.domain.QFollow.follow;
import static com.example.villagerservice.member.domain.QMember.member;
import static com.example.villagerservice.member.domain.QMemberDetail.memberDetail;

@Repository
@RequiredArgsConstructor
public class FollowQueryRepositoryImpl implements FollowQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public FollowList.Response getFollowList(FollowList.Request request) {

        StringPath aliasQuantity = Expressions.stringPath("followCount");

        List<FollowListItem> result = queryFactory
                .select(Projections.constructor(FollowListItem.class,
                        memberDetail.nickname,
                        follow.toMember.count().as("followCount"))
                )
                .from(follow)
                .innerJoin(follow.toMember, member)
                .innerJoin(member.memberDetail, memberDetail)
                .where(isActiveMember())
                .groupBy(follow.toMember)
                .limit(request.getSize())
                .offset(request.getOffset())
                .orderBy(aliasQuantity.desc())
                .fetch();

        return FollowList.Response.builder()
                .pageNumber(request.getPage())
                .follows(result)
                .build();
    }
    private BooleanExpression isActiveMember() {
        return member.isDeleted.eq(false);
    }
}
