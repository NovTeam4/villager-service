package com.example.villagerservice.member.domain;

import com.example.villagerservice.town.domain.Town;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTown {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_town_id")
    private Long id;

    private String townName;

    @Embedded
    private TownLocation townLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "town_id")
    private Town town;

    private MemberTown(String townName, TownLocation townLocation, Member member, Town town) {
        this.townName = townName;
        this.townLocation = townLocation;
        this.member = member;
        this.town = town;
    }

    public static MemberTown createMemberTown(Member member, Town town, String townName, TownLocation townLocation) {
        return new MemberTown(townName, townLocation, member, town);
    }
}
