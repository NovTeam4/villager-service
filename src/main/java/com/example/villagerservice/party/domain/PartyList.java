package com.example.villagerservice.party.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PartyList {

    @Id @GeneratedValue
    @Column(name = "party_list_id")
    private Long id;

    @Column(name = "red_dt")
    private LocalDateTime regDt;

    private boolean isAccept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

}
