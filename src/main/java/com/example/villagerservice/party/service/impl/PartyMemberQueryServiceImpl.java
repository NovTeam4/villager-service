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

    @Override
    public List<Long> getPartyMemberId(Long partyId, String email) {
        return partyMemberQueryRepository.getPartyMemberId(partyId, email);
    }
}
