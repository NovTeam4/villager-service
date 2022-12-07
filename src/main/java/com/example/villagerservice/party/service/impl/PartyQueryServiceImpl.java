package com.example.villagerservice.party.service.impl;

import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.exception.PartyApplyException;
import com.example.villagerservice.party.repository.PartyQueryRepository;
import com.example.villagerservice.party.service.PartyQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.villagerservice.party.exception.PartyApplyErrorCode.PARTY_NOT_FOUND;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyQueryServiceImpl implements PartyQueryService {

    private final PartyQueryRepository partyQueryRepository;

    @Override
    public PartyDTO.Response getParty(Long partyId) {

        return partyQueryRepository.getParty(partyId).orElseThrow(
                () -> new PartyApplyException(PARTY_NOT_FOUND)
        );

    }
}
