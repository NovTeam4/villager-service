package com.example.villagerservice.party.domain;

import com.example.villagerservice.common.domain.BaseTimeEntity;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.party.request.PartyCreate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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

    @Builder
    public Party(PartyCreate partyCreate, Member member) {
        this.partyName = partyCreate.getPartyName();
        this.score = partyCreate.getScore();
        this.startDt = partyCreate.getStartDt();
        this.endDt = partyCreate.getEndDt();
        this.amount = partyCreate.getAmount();
        this.member = member;
    }

    public Party() {

    }
}
