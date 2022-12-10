package com.example.villagerservice.member.infra;

import com.example.villagerservice.member.domain.MemberTownQueryRepository;
import com.example.villagerservice.member.dto.FindMemberTownDetail;
import com.example.villagerservice.member.dto.FindMemberTownList;
import com.example.villagerservice.member.dto.MemberTownListItem;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.villagerservice.member.domain.QMemberTown.memberTown;
import static com.example.villagerservice.town.domain.QTown.town1;

@Repository
@RequiredArgsConstructor
public class MemberTownQueryRepositoryImpl implements MemberTownQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public FindMemberTownList.Response getMemberTownList(Long memberId) {

        List<MemberTownListItem> result = queryFactory
                .select(Projections.constructor(MemberTownListItem.class,
                        memberTown.id, memberTown.townName,
                        town1.city, town1.town, town1.village,
                        memberTown.createdAt,
                        memberTown.modifiedAt))
                .from(memberTown)
                .join(memberTown.town, town1)
                .where(memberTown.member.id.eq(memberId))
                .orderBy(memberTown.id.asc())
                .fetch();

        return FindMemberTownList.Response.builder()
                .towns(result)
                .build();
    }

    @Override
    public FindMemberTownDetail.Response getMemberTownDetail(Long memberTownId) {
        return queryFactory
                .select(Projections.constructor(FindMemberTownDetail.Response.class,
                        memberTown.id, memberTown.townName,
                        memberTown.townLocation.latitude, memberTown.townLocation.longitude,
                        town1.city, town1.town, town1.village,
                        memberTown.createdAt, memberTown.modifiedAt))
                .from(memberTown)
                .join(memberTown.town, town1)
                .where(memberTown.id.eq(memberTownId))
                .fetchOne();
    }
}
