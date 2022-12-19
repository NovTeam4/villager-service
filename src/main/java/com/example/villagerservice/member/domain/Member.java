package com.example.villagerservice.member.domain;

import com.example.villagerservice.common.domain.BaseTimeEntity;
import com.example.villagerservice.config.security.oauth2.enums.SocialType;
import com.example.villagerservice.member.exception.MemberException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MemberDetail memberDetail;

    private String email;
    private String encodedPassword;
    private boolean isDeleted;
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private RoleType roleType;

    @Embedded
    private TagCollection tagCollection = new TagCollection();

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Builder
    private Member(String nickname, String email, String encodedPassword, Gender gender, Birthday birthday, String introduce, SocialType socialType) {
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.isDeleted = false;
        this.roleType = RoleType.USER;
        this.socialType = socialType;
        addMemberDetail(nickname, gender, birthday, introduce);
    }

    private void addMemberDetail(String nickname, Gender gender, Birthday birthday, String introduce) {
        if (this.memberDetail == null) {
            this.memberDetail = new MemberDetail();
        }
        this.memberDetail.addDetail(this, nickname, gender, birthday, introduce);
    }

    public void updateMemberInfo(String nickname, String introduce) {
        if(this.memberDetail != null) {
            this.memberDetail.updateMemberInfo(nickname, introduce);
        }
    }

    public boolean checkPasswordValid(PasswordEncoder passwordEncoder, String rawPassword) {
        if (!StringUtils.hasText(rawPassword)) {
            throw new MemberException(MEMBER_VALID_NOT);
        }
        return passwordEncoder.matches(rawPassword, this.encodedPassword);
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

    public void addMemberAttentionTag(List<Tag> tags) {
        this.tagCollection.addTags(tags);
    }

    public void setJwtMemberId(Long memberId) {
        this.id = memberId;
    }
}
