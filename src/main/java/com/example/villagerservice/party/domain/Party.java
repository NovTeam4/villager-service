package com.example.villagerservice.party.domain;

import com.example.villagerservice.common.domain.BaseTimeEntity;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.dto.UpdatePartyDTO;
import com.example.villagerservice.party.request.PartyCreate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Party extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;

    @Column(name = "party_name")
    private String partyName;

    private Integer score;

    @Column(name = "start_dt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDt;

    @Column(name = "end_dt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDt;
    private Integer amount;

    private Integer numberPeople;

    private String location;

    private Double latitude;

    private Double longitude;

    private String content;

    //Member와 연결 필요
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "party" , cascade = CascadeType.ALL)
    private List<PartyTag> tagList = new ArrayList<>();

    public static Party createParty(PartyDTO.Request request, Member member) {
        Party party = Party.builder()
                .partyName(request.getPartyName())
                .score(request.getScore())
                .startDt(request.getStartDt())
                .endDt(request.getEndDt())
                .amount(request.getAmount())
                .numberPeople(request.getNumberPeople())
                .location(request.getLocation())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .content(request.getContent())
                .tagList(request.getTagList())
                .member(member)
                .build();

        party.updateTagInfo();
        return party;
    }

    public void updatePartyInfo(UpdatePartyDTO.Request request) {

        if(request.getPartyName() != null) {
            this.partyName = request.getPartyName();
        }

        if(request.getScore() != null) {
            this.score = request.getScore();
        }

        if(request.getStartDt() != null) {
            this.startDt = request.getStartDt();
        }

        if(request.getEndDt() != null) {
            this.endDt = request.getEndDt();
        }

        if (request.getAmount() != null) {
            this.amount = request.getAmount();
        }

        if (request.getNumberPeople() != null) {
            this.numberPeople = request.getNumberPeople();
        }

        if (request.getLocation() != null) {
            this.location = request.getLocation();
        }

        if (request.getLatitude() != null) {
            this.latitude = request.getLatitude();
        }

        if (request.getLongitude() != null) {
            this.longitude = request.getLongitude();
        }

        if (request.getContent() != null) {
            this.content = request.getContent();
        }

        if (request.getTagList() != null) {
            this.tagList = request.getTagList();
            updateTagInfo();
        }
    }

    private void updateTagInfo(){
        for (PartyTag partyTag : tagList) {
            partyTag.updateParty(this);
        }
    }
}
