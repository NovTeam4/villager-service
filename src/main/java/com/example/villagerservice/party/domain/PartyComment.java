package com.example.villagerservice.party.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PartyComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_comment_id")
    private Long id;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;


    public static PartyComment createPartyComment(String contents , Party party) {

        return PartyComment.builder()
                .contents(contents)
                .party(party)
                .build();
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
