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
import com.example.villagerservice.party.dto.PartyApplyDto;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.dto.UpdatePartyDTO;
import com.example.villagerservice.party.exception.PartyErrorCode;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.repository.PartyApplyRepository;
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
        // 모임장인지 검사
        Party party = partyRepository.findById(partyId).orElseThrow(
            () -> new PartyException(PARTY_NOT_FOUND)
        );
        if(party.getMember().getId() != member.getId()){
            throw new PartyException(PARTY_NOT_FOUND_MEMBER);
        }

        // 시작시간이 넘었는지 검사
        if(party.getStartDt().isBefore(LocalDate.now())){
            throw new PartyException(PARTY_IS_NOT_TIME);
        }

        // 파티신청자 불러오기
        List<PartyApply> partyApplyList = partyApplyQueryService.getPartyApplyId(partyId, member.getId());
        // 허락신청자 블러오기
        List<PartyApply> acceptPartyApplyList = new ArrayList<>();
        for(PartyApply partyApply: partyApplyList){
            if(partyApply.isAccept()){
                acceptPartyApplyList.add(partyApply);
            }
        }
        // 허락된 멤버가 한명도 없을 경우
        if(acceptPartyApplyList.size() == 0){
            throw new PartyException(PARTY_MEMBER_EMPTY);
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
