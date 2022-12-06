package com.example.villagerservice.party.service.impl;

import static com.example.villagerservice.party.exception.PartyErrorCode.PARTY_NOT_FOUND;
import static com.example.villagerservice.party.exception.PartyErrorCode.PARTY_NOT_FOUND_MEMBER;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;

    @Override
    public void createParty(Long memberId , PartyDTO.Request partyRequest) {

        Member member = memberCheckedById(memberId);
        Party party = Party.createParty(partyRequest.getPartyName(), partyRequest.getScore(), partyRequest.getStartDt(), partyRequest.getEndDt(), partyRequest.getAmount(), member);
        partyRepository.save(party);

    }

    @Override
    public PartyDTO.Response getParty(Long partyId) {

        Party party = partyRepository.findById(partyId).orElseThrow(
                () -> new PartyException(PARTY_NOT_FOUND)
        );

        return PartyDTO.Response.createPartyResponse(party);

    }

    private Member memberCheckedById(Long memberId) {

        return memberRepository.findById(memberId).orElseThrow(
                () -> new PartyException(PARTY_NOT_FOUND_MEMBER)
        );
    }
}
