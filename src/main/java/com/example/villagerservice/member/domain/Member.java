package com.example.villagerservice.member.domain;

import com.example.villagerservice.common.domain.BaseTimeEntity;
import com.example.villagerservice.member.exception.MemberException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_UPDATE_SAME_PASS;
import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_VALID_NOT;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "email"
        }))
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String nickname;
    private String email;
    private String encodedPassword;
    private boolean isDeleted;
    private LocalDateTime deletedAt;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Builder
    private Member(String nickname, String email, String encodedPassword) {
        this.nickname = nickname;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.roleType = RoleType.USER;
        this.isDeleted = false;
    }

    public void updateMemberInfo(String nickname) {
        if (!StringUtils.hasText(nickname)) {
            throw new MemberException(MEMBER_VALID_NOT);
        }

        if (!this.nickname.equals(nickname)) {
            this.nickname = nickname;
        }
    }

    public void changePassword(PasswordEncoder passwordEncoder, String rawPassword) {
        if (!StringUtils.hasText(rawPassword)) {
            throw new MemberException(MEMBER_VALID_NOT);
        }

        if (passwordEncoder.matches(rawPassword, this.encodedPassword)) {
            throw new MemberException(MEMBER_UPDATE_SAME_PASS);
        }

        this.encodedPassword = passwordEncoder.encode(rawPassword);
    }

    public void deleteMember() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
