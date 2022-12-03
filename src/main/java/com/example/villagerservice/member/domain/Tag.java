package com.example.villagerservice.member.domain;

import com.example.villagerservice.member.exception.MemberException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_VALID_NOT;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    private String name;
    public Tag(String name) {
        validTagName(name);
        this.name = name;
    }
    private void validTagName(String name) {
        if (name.isBlank())
            throw new MemberException(MEMBER_VALID_NOT);
    }
}
