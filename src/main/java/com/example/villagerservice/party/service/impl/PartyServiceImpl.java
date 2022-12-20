package com.example.villagerservice.party.service.impl;

import com.example.villagerservice.config.events.Events;
import com.example.villagerservice.events.service.PartyCreatedEventService;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyComment;

import com.example.villagerservice.party.domain.PartyTag;
import com.example.villagerservice.party.dto.PartyCommentDTO;

import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.dto.UpdatePartyDTO;
import com.example.villagerservice.party.domain.PartyCreatedEvent;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.repository.PartyTagRepository;
import com.example.villagerservice.party.service.PartyCommentService;
import com.example.villagerservice.party.service.PartyLikeService;
import com.example.villagerservice.party.service.PartyService;
import com.example.villagerservice.town.domain.TownQueryRepository;
import com.example.villagerservice.town.domain.TownRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

import static com.example.villagerservice.party.exception.PartyErrorCode.*;


@Service
@RequiredArgsConstructor
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;
    private final PartyTagRepository partyTagRepository;
    private final PartyCommentService partyCommentService;
    private final PartyCreatedEventService partyCreatedEventService;

    private final PartyLikeService partyLikeService;
    @Override
    @Transactional
    public void createParty(Long memberId , PartyDTO.Request partyRequest) {
        Member member = memberCheckedById(memberId);
        Party party = Party.createParty(partyRequest, member);
        partyRepository.save(party);

        partyCreatedEventService.raise(
                partyRequest.getLatitude(),
                partyRequest.getLongitude(),
                partyRequest.getScore(),
                partyRequest.getNumberPeople(),
                party.getId(),
                partyRequest.getAmount(),
                partyRequest.getPartyName(),
                partyRequest.getTagList());
    }

    @Override
    @Transactional
    public PartyDTO.Response updateParty(Long partyId , UpdatePartyDTO.Request updatePartyRequest , String email) {
        Party party = partyCheckedById(partyId);
        return updatePartyInfo(party ,updatePartyRequest , email);
    }

    @Override
    public PartyDTO.Response getParty(Long partyId , String email) {
        Party party = partyCheckedById(partyId);
        List<PartyComment> commentList = partyCommentService.getAllComment(partyId);
        boolean partyLike = partyLikeService.isPartyLike(partyId, email);

        System.out.println("party.getMember().getId() = " + party.getMember().getId());
        return PartyDTO.Response.createPartyResponse(party , commentList , partyLike);
    }

    @Override
    @Transactional
    public void deleteParty(Long partyId) {
        Party party = partyCheckedById(partyId);
        partyCommentService.deleteAllComment(party.getId());
        partyTagRepository.deleteAllByParty_id(party.getId());
        partyRepository.deleteById(party.getId());
    }

    private Member memberCheckedById(Long memberId) {

        return memberRepository.findById(memberId).orElseThrow(
                () -> new PartyException(PARTY_NOT_FOUND_MEMBER)
        );
    }

    private Party partyCheckedById(Long partyId) {

        return partyRepository.findById(partyId).orElseThrow(
                () -> new PartyException(PARTY_NOT_FOUND)
        );

    }

    private PartyDTO.Response updatePartyInfo(Party party , UpdatePartyDTO.Request updatePartyRequest , String email) {
        partyTagRepository.deleteAllByParty_id(party.getId());
        List<PartyComment> commentList = partyCommentService.getAllComment(party.getId());
        boolean partyLike = partyLikeService.isPartyLike(party.getId(), email);
        party.updatePartyInfo(updatePartyRequest);
        return PartyDTO.Response.createPartyResponse(party , commentList , partyLike);
    }

}
