package com.example.villagerservice.party.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
public class PartyCommentDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{

        @NotEmpty
        private String contents;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{

        @NotEmpty
        private String contents;

        private Long partyCommentId;

        private Long partyId;

        private String nickName;

        private boolean isOwner;

    }
}
