package com.example.villagerservice.member.domain;

import com.example.villagerservice.common.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String nickname;
    private String email;
    private String pass;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Builder
    private Member(String nickname, String email, String pass) {
        this.nickname = nickname;
        this.email = email;
        this.pass = pass;
        this.roleType = RoleType.USER;
    }
}
