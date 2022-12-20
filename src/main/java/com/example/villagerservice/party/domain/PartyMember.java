package com.example.villagerservice.party.domain;

import com.example.villagerservice.common.domain.BaseTimeEntity;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.party.type.PartyMemberRolesType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyMember extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    public static PartyMember createPartyMember(PartyApply acceptPartyApply) {
        return PartyMember.builder()
            .memberId(acceptPartyApply.getTargetMemberId())
            .party(acceptPartyApply.getParty())
            .build();
    }
}
