package com.example.villagerservice.party.domain;

import com.example.villagerservice.common.domain.BaseTimeEntity;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.party.request.PartyCreate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Party extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "party_id")
    private Long id;

    @Column(name = "party_name")
    private String partyName;

    private Integer score;

    @Column(name = "start_dt")
    private LocalDateTime startDt;

    @Column(name = "end_dt")
    private LocalDateTime endDt;

    private Integer amount;

    //Member와 연결 필요
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Party createParty(String partyName , Integer score , LocalDateTime startDt , LocalDateTime endDt , Integer amount , Member member) {
        return Party.builder()
                .partyName(partyName)
                .score(score)
                .startDt(startDt)
                .endDt(endDt)
                .amount(amount)
                .member(member)
                .build();
    }
}
