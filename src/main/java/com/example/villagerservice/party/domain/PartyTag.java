package com.example.villagerservice.party.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartyTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_tag_id")
    private Long id;

    @Column(name = "tag_name")
    @NotEmpty
    private String tagName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    public void updateParty(Party party){
        this.party = party;
    }

}
