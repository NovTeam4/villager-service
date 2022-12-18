package com.example.villagerservice.party.service.impl;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyLike;
import com.example.villagerservice.party.exception.PartyErrorCode;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.repository.PartyLikeRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.request.PartyLikeDto;
import com.example.villagerservice.party.service.PartyLikeService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PartyLikeServiceImpl implements PartyLikeService {
    private final PartyLikeRepository partyLikeRepository;
    private final PartyRepository partyRepository;

    @Override
    public PartyLikeDto.Response partyLike(Long partyId, Member member) {
        // 모임이 존재하는지 검사
        Party party = partyRepository.findById(partyId).orElseThrow(
            () -> new PartyException(PartyErrorCode.PARTY_NOT_FOUND)
        );

        // 좋아요가 존재하는지 검사
        Optional<PartyLike> optionalPartyLike =
            partyLikeRepository.findByParty_IdAndMember_Email(partyId, member.getEmail());

        // 이미 좋아요 존재하면 삭제 후, false 리턴
        if(optionalPartyLike.isPresent()){
            partyLikeRepository.delete(optionalPartyLike.get());
            return PartyLikeDto.Response.toDto(false);
        }

        // 존재하지 않으면 추가 후, true 리턴
        partyLikeRepository.save(PartyLike.builder()
                .party(party)
                .member(member)
                .build());
        return PartyLikeDto.Response.toDto(true);
    }

    @Override
    public boolean isPartyLike(Long partyId, String email) {
        return partyLikeRepository.existByPartyIdAndMemberEmail(partyId, email);
    }
}
