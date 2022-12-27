package com.example.villagerservice.party.repository.impl;

import com.example.villagerservice.party.domain.QPartyMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PartyMemberQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Long> getPartyMemberId(Long partyId) {
        QPartyMember p = QPartyMember.partyMember;

        List<Long> result = queryFactory
            .select(p.id)
            .from(p)
            .where(p.party.id.eq(partyId))
            .fetch();

        return result;
    }
}
