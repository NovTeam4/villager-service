package com.example.villagerservice.party.domain;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class PartyTag {

    @Id @GeneratedValue
    @Column(name = "party_tag_id")
    private Long id;

    @Column(name = "tag_name")
    @NotEmpty
    private String tagName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

}
