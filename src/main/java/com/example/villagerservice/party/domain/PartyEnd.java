package com.example.villagerservice.party.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class PartyEnd {

    @Id @GeneratedValue
    @Column(name = "party_end_id")
    private Long id;

    @Column(name = "end_dt")
    private LocalDateTime endDt;

    //모임 연관성 추가

    //회원 연관성 추가
}
