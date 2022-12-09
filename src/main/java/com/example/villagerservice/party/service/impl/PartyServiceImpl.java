package com.example.villagerservice.party.service.impl;

import static com.example.villagerservice.party.exception.PartyErrorCode.PARTY_NOT_FOUND;
import static com.example.villagerservice.party.exception.PartyErrorCode.PARTY_NOT_FOUND_MEMBER;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.dto.UpdatePartyDTO;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    @Transactional
    public PartyDTO.Response updateParty(Long partyId , UpdatePartyDTO.Request updatePartyRequest) {
        partyCheckedById(partyId);
        Party party = partyRepository.findById(partyId).get();
        return updatePartyInfo(party ,updatePartyRequest);
    }

    @Override
    public void deleteParty(Long partyId) {
        partyCheckedById(partyId);
        partyRepository.deleteById(partyId);
    }

    private Member memberCheckedById(Long memberId) {

        return memberRepository.findById(memberId).orElseThrow(
                () -> new PartyException(PARTY_NOT_FOUND_MEMBER)
        );
    }

    private void partyCheckedById(Long partyId) {

        partyRepository.findById(partyId).orElseThrow(
                () -> new PartyException(PARTY_NOT_FOUND)
        );

    }

    private PartyDTO.Response updatePartyInfo(Party party , UpdatePartyDTO.Request updatePartyRequest) {
        party.updatePartyInfo(updatePartyRequest);

        return PartyDTO.Response.createPartyResponse(party);
    }
}
