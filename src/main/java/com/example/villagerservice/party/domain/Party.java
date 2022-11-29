package com.example.villagerservice.party.domain;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Party {

    @Id @GeneratedValue
    @Column(name = "party_id")
    private Long id;

    @NotEmpty
    @Column(name = "party_name")
    private String partyName;

    @NotEmpty
    private Integer score;

    @Column(name = "start_dt")
    private LocalDateTime startDt;

    @Column(name = "end_dt")
    private LocalDateTime endDt;

    @Size(min = 10000 , max = 30000)
    private Integer amount;

    //Member와 연결 필요

}
