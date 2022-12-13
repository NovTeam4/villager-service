package com.example.villagerservice.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "nickname"
        }))
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

    private String introduce;

    public void addDetail(Member member, String nickname, Gender gender, Birthday birthday, String introduce) {
        this.nickname = nickname;
        this.gender = gender;
        this.birthday = birthday;
        this.mannerPoint = new MannerPoint(50);
        this.member = member;
        if(introduce == null) {
            this.introduce = null;
        } else  {
            this.introduce = (introduce.isBlank()) ? null : introduce;
        }
    }

    public void updateMemberInfo(String nickname, String introduce) {
        nickNameUpdate(nickname);
        introduceUpdate(introduce);
    }

    private void nickNameUpdate(String nickname) {
        if(StringUtils.hasText(nickname)) {
            if (!this.nickname.equals(nickname)) {
                this.nickname = nickname;
            }
        }
    }

    private void introduceUpdate(String introduce) {
        if(StringUtils.hasText(introduce)) {
            if(this.introduce == null) {
                this.introduce = introduce;
            }
            if (!this.introduce.equals(introduce)) {
                this.introduce = introduce;
            }
        }
    }
}
