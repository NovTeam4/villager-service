package com.example.villagerservice.party.service.impl;

import static com.example.villagerservice.party.exception.PartyApplyErrorCode.*;

import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyApply;
import com.example.villagerservice.party.exception.PartyApplyException;
import com.example.villagerservice.party.repository.PartyApplyRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.dto.PartyApplyDto;
import com.example.villagerservice.party.dto.PartyApplyDto.Response;
import com.example.villagerservice.party.service.PartyApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyApplyServiceImpl implements PartyApplyService {
    private final PartyRepository partyRepository;
    private final PartyApplyRepository partyApplyRepository;

    @Override
    public PartyApplyDto.Response applyParty(Long targetMemberId, Long partyId) {
        Party party = partyRepository.findById(partyId).orElseThrow(
            () -> new PartyApplyException(PARTY_NOT_FOUND)
        );
        if(partyApplyRepository.existsByTargetMemberIdAndParty_Id(targetMemberId, partyId)){
            throw new PartyApplyException(ALREADY_BEAN_APPLIED);
        }

        return Response.toDto(partyApplyRepository.save(PartyApply.createPartyList(party, targetMemberId)));
    }

    @Override
    public Page<Response> getApplyPartyList(Long partyId, Pageable pageable) {
        if(!partyRepository.existsById(partyId)){
            throw new PartyApplyException(PARTY_NOT_FOUND);
        }

        return partyApplyRepository.findByParty_Id(partyId, pageable)
            .map(partyApply -> Response.toDto(partyApply));
    }

    @Override
    public Response partyPermission(Long partyId, Long targetMemberId, String email) {
        // 모임 가져오기
        Party party = partyRepository.findById(partyId).orElseThrow(
            () -> new PartyApplyException(PARTY_NOT_FOUND)
        );
        // 가져온 모임이 주최자의 이메일과 다르다면 에러
        if(!party.getMember().getEmail().equals(email)){
            throw new PartyApplyException(DIFFERENT_HOST);
        }
        // 알맞은 모임신청 가져오기
        PartyApply partyApply = partyApplyRepository.findByParty_IdAndTargetMemberId(partyId, targetMemberId).orElseThrow(
            () -> new PartyApplyException(PARTY_APPLY_NOT_FOUND)
        );
        // 만약 이미 신청된 상태이면 에러 반환
        if(partyApply.isAccept()){
            throw new PartyApplyException(ALREADY_ACCEPT_APPLY);
        }

        // 허가 후 저장
        partyApply.setAccept(true);
        return PartyApplyDto.Response.toDto(partyApplyRepository.save(partyApply));
    }
}
