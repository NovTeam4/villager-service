package com.example.villagerservice.party.service.impl;

import static com.example.villagerservice.party.exception.PartyListErrorCode.*;

import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyList;
import com.example.villagerservice.party.exception.PartyErrorCode;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.exception.PartyListErrorCode;
import com.example.villagerservice.party.exception.PartyListException;
import com.example.villagerservice.party.repository.PartyListRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.service.PartyListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PartyListServiceImpl implements PartyListService {
    private final PartyRepository partyRepository;
    private final PartyListRepository partyListRepository;

    @Override
    public void applyParty(String email, Long partyId) {
        Party party = partyRepository.findById(partyId).orElseThrow(
            () -> new PartyListException(PARTY_NOT_FOUND)
        );
        if(partyListRepository.existsByParty_Member_EmailAndParty_Id(email, partyId)){
            throw new PartyListException(ALREADY_BEAN_APPLIED);
        }

        partyListRepository.save(PartyList.createPartyList(party));
    }
}
