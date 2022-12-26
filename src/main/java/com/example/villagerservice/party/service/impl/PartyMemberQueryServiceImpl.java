package com.example.villagerservice.party.service.impl;

import com.example.villagerservice.party.repository.impl.PartyMemberQueryRepository;
import com.example.villagerservice.party.service.PartyMemberQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyMemberQueryServiceImpl implements PartyMemberQueryService {
    private final PartyMemberQueryRepository partyMemberQueryRepository;

    /**
     * 모임원 id 조회
     * @param partyId
     * @return
     */
    @Override
    public List<Long> getPartyMemberId(Long partyId) {
        return partyMemberQueryRepository.getPartyMemberId(partyId);
    }
}
