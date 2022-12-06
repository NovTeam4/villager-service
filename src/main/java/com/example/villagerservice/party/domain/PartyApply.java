package com.example.villagerservice.party.domain;

import com.example.villagerservice.common.domain.BaseTimeEntity;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PartyApply extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "party_list_id")
    private Long id;

    private Long targetMemberId;

    private boolean isAccept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    public static PartyApply createPartyList(Party party){
        return PartyApply.builder()
            .targetMemberId(1L)
            .party(party)
            .isAccept(false)
            .build();
    }
}
