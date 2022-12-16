package com.example.villagerservice.party.service.impl;

import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyComment;
import com.example.villagerservice.party.exception.PartyCommentErrorCode;
import com.example.villagerservice.party.exception.PartyCommentException;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.repository.PartyCommentRepository;
import com.example.villagerservice.party.repository.PartyRepository;
import com.example.villagerservice.party.service.PartyCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.villagerservice.party.exception.PartyErrorCode.PARTY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PartyCommentServiceImpl implements PartyCommentService {

    private final PartyCommentRepository partyCommentRepository;

    private final PartyRepository partyRepository;

    @Override
    public void createComment(Long partyId, String contents) {
        Party party = partyCheckedById(partyId);

        if(!StringUtils.hasText(contents)){
            throw new PartyCommentException(PartyCommentErrorCode.CONTENT_IS_REQUIRED);
        }
        PartyComment partyComment = PartyComment.createPartyComment(contents, party);

        partyCommentRepository.save(partyComment);
    }

    private Party partyCheckedById(Long partyId) {

        return partyRepository.findById(partyId).orElseThrow(
                () -> new PartyException(PARTY_NOT_FOUND)
        );
    }

    @Override
    public List<PartyComment> getAllComment(Long partyId) {
        return partyCommentRepository.findAllByParty_id(partyId);
    }
}
