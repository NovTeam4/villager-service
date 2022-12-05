package com.example.villagerservice.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail {
    @Id
    @Column(name = "member_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "member_id")
    private Member member;
    private String nickname;
    @Enumerated(EnumType.STRING)
    @Column(length = 5)
    private Gender gender;
    @Embedded
    private Birthday birthday;
    @Embedded
    private MannerPoint mannerPoint;

    public void nickNameUpdate(String nickname) {
        if (!this.nickname.equals(nickname)) {
            this.nickname = nickname;
        }
    }

    public void addDetail(Member member, String nickname, Gender gender, Birthday birthday) {
        this.nickname = nickname;
        this.gender = gender;
        this.birthday = birthday;
        this.mannerPoint = new MannerPoint(50);
        this.member = member;
    }
}
