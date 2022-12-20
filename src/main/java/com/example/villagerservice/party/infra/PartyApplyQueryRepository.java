package com.example.villagerservice.party.infra;

import com.example.villagerservice.party.domain.PartyApply;
import com.example.villagerservice.party.domain.QPartyApply;
import com.example.villagerservice.party.dto.PartyApplyDto;
import com.example.villagerservice.party.service.PartyApplyQueryService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PartyApplyQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<PartyApply> getPartyApply(Long partyId, Long memberId) {
        QPartyApply p = QPartyApply.partyApply;

        List<PartyApply> result = queryFactory
            .select(p)
            .from(p)
            .where(p.party.id.eq(partyId)
                .and(p.party.member.id.eq(memberId)))
            .fetch();

        return result;
    }
}
