package com.example.villagerservice.party.domain;

import javax.persistence.*;

@Entity
public class PartyComment {

    @Id @GeneratedValue
    @Column(name = "party_comment_id")
    private Long id;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;


}
