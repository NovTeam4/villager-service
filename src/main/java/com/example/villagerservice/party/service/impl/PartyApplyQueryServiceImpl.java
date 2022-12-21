package com.example.villagerservice.party.service.impl;

import com.example.villagerservice.party.domain.PartyApply;
import com.example.villagerservice.party.infra.PartyApplyQueryRepository;
import com.example.villagerservice.party.service.PartyApplyQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyApplyQueryServiceImpl implements PartyApplyQueryService {
    private final PartyApplyQueryRepository partyApplyQueryRepository;

    @Override
    public List<PartyApply> getPartyApplyId(Long partyId, String email) {
        return partyApplyQueryRepository.getPartyApply(partyId, email);
    }
}
