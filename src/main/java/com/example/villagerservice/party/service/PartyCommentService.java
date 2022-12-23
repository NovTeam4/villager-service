package com.example.villagerservice.party.service;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.party.domain.PartyComment;

import java.util.List;

public interface PartyCommentService {

    public void createComment(Long partyId , String contents , Member member);

    List<PartyComment> getAllComment(Long partyId);

    void deleteAllComment(Long partyId);

    String updateComment(Long partyCommentId , String contents);

}
