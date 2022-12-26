package com.example.villagerservice.party.repository.impl;

import com.example.villagerservice.party.domain.PartyApply;
import com.example.villagerservice.party.domain.QPartyApply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PartyMemberQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Long> getPartyMemberId(Long partyId) {
        QPartyApply p = QPartyApply.partyApply;

        List<Long> result = queryFactory
            .select(p.id)
            .from(p)
            .where(p.party.id.eq(partyId))
            .fetch();

        return result;
    }
}
