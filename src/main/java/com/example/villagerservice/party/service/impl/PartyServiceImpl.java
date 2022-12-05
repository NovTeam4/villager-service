package com.example.villagerservice.party.service.impl;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.exception.PartyListException;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.request.PartyCreate;
import com.example.villagerservice.party.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.villagerservice.party.exception.PartyErrorCode.PARTY_NOT_FOUND_MEMBER;
import static com.example.villagerservice.party.exception.PartyListErrorCode.PARTY_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;

    @Override
    public void createParty(String email , PartyCreate partyCreate) {

        Member member = memberCheckedByEmail(email);
        Party party = new Party(partyCreate , member);

        partyRepository.save(party);

    }

    @Override
    public PartyDTO getParty(Long partyId) {

        Party party = partyRepository.findById(partyId).orElseThrow(
                () -> new PartyListException(PARTY_NOT_FOUND)
        );

        return PartyDTO.createPartyDTO(party);

    }

    private Member memberCheckedByEmail(String email) {

        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if(optionalMember.isEmpty()) {
            throw new PartyException(PARTY_NOT_FOUND_MEMBER);
        }

        return optionalMember.get();
    }
}
