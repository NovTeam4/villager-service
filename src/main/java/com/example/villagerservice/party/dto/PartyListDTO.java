package com.example.villagerservice.party.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartyListDTO {

    private Long partyId;

    private String partyName;

    private LocalDate startDt;

    private LocalDate endDt;

    private String nickname;

    private String content;

    private String location;

    private List<String> tagNameList;

}
