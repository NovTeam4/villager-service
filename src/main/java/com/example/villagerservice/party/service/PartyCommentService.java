package com.example.villagerservice.party.service;

import com.example.villagerservice.party.domain.PartyComment;

import java.util.List;

public interface PartyCommentService {

    public void createComment(Long partyId , String contents);

    List<PartyComment> getAllComment(Long partyId);

    void deleteAllComment(Long partyId);

    String updateComment(Long partyCommentId , String contents);

}
