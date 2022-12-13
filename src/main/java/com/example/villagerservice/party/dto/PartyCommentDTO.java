package com.example.villagerservice.party.dto;

import com.example.villagerservice.party.domain.Party;
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

        private Party party;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{

        @NotEmpty
        private String contents;

        private Party party;

    }
}
