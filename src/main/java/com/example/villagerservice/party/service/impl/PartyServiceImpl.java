package com.example.villagerservice.party.service.impl;

import static com.example.villagerservice.party.exception.PartyErrorCode.*;
import static com.example.villagerservice.party.exception.PartyErrorCode.PARTY_IS_NOT_TIME;
import static com.example.villagerservice.party.exception.PartyErrorCode.PARTY_NOT_FOUND;
import static com.example.villagerservice.party.exception.PartyErrorCode.PARTY_NOT_FOUND_MEMBER;

import com.example.villagerservice.events.service.PartyCreatedEventService;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyApply;
import com.example.villagerservice.party.domain.PartyComment;
import com.example.villagerservice.party.domain.PartyMember;
import com.example.villagerservice.party.dto.PartyApplyDto;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.dto.UpdatePartyDTO;
import com.example.villagerservice.party.exception.PartyErrorCode;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.repository.PartyApplyRepository;
import com.example.villagerservice.party.repository.PartyMemberRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.repository.PartyTagRepository;
import com.example.villagerservice.party.service.PartyApplyQueryService;
import com.example.villagerservice.party.service.PartyCommentService;
import com.example.villagerservice.party.service.PartyLikeService;
import com.example.villagerservice.party.service.PartyService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;
    private final PartyTagRepository partyTagRepository;
    private final PartyCommentService partyCommentService;
    private final PartyCreatedEventService partyCreatedEventService;
    private final PartyLikeService partyLikeService;
    private final PartyApplyQueryService partyApplyQueryService;
    private final PartyApplyRepository partyApplyRepository;
    private final PartyMemberRepository partyMemberRepository;

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
    public void startParty(Long partyId, Member member) {
        // ??????????????? ??????
        Party party = partyRepository.findById(partyId).orElseThrow(
            () -> new PartyException(PARTY_NOT_FOUND)
        );
        if(!party.getMember().getEmail().equals(member.getEmail())){
            throw new PartyException(PARTY_NOT_FOUND_MEMBER);
        }

        // ??????????????? ???????????? ??????
        if(party.getStartDt().isAfter(LocalDate.now())){
            throw new PartyException(PARTY_IS_NOT_TIME);
        }

        // ??????????????? ????????????
        List<PartyApply> partyApplyList = partyApplyQueryService.getPartyApplyId(partyId, member.getEmail());
        // ??????????????? ????????????
        List<PartyApply> acceptPartyApplyList = new ArrayList<>();
        for(PartyApply partyApply: partyApplyList){
            if(partyApply.isAccept()){
                acceptPartyApplyList.add(partyApply);
            }
        }
        // ????????? ????????? ????????? ?????? ??????
        if(acceptPartyApplyList.size() == 0){
            throw new PartyException(PARTY_MEMBER_EMPTY);
        }

        // ?????? ????????? ??????
        for(PartyApply partyApply: partyApplyList){
            partyApplyRepository.delete(partyApply);
        }

        // ????????? ????????? insert
        for(PartyApply acceptPartyApply: acceptPartyApplyList){
            partyMemberRepository.save(PartyMember.createPartyMember(acceptPartyApply));
        }
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
