package com.example.villagerservice.party.service.impl;

import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.dto.PartyListDTO;
import com.example.villagerservice.party.repository.PartyQueryRepository;
import com.example.villagerservice.party.service.PartyQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartyQueryServiceImpl implements PartyQueryService {

    private final PartyQueryRepository partyQueryRepository;

    @Override
    public List<PartyListDTO> getPartyList(String email ,Double LAT, Double LNT) {
        return partyQueryRepository.getPartyList(email , LAT , LNT);
    }
}
