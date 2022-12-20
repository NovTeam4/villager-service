package com.example.villagerservice.party.dto;

import com.example.villagerservice.party.domain.PartyApply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class PartyApplyDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Response{
        private Long id;

        private Long targetMemberId;

        private boolean isAccept;

        private Long partyId;

        public static PartyApplyDto.Response toDto(PartyApply partyApply){
            return PartyApplyDto.Response.builder()
                .id(partyApply.getId())
                .targetMemberId(partyApply.getTargetMemberId())
                .isAccept(partyApply.isAccept())
                .partyId(partyApply.getParty().getId())
                .build();
        }
    }
}
