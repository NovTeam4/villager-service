package com.example.villagerservice.party.dto;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.domain.PartyList;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartyDTO {

    private String partyName;

    private Integer score;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startDt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endDt;

    private Integer amount;

    private Member member;

    public static PartyDTO createPartyDTO(Party party){
        return PartyDTO.builder()
                .partyName(party.getPartyName())
                .score(party.getScore())
                .amount(party.getAmount())
                .startDt(party.getStartDt())
                .endDt(party.getEndDt())
                .member(party.getMember())
                .build();
    }
}
