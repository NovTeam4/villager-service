package com.example.villagerservice.member.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MannerPoint {
    @Column(length = 2)
    private int point;
    public MannerPoint(int point) {
        this.point = point;
    }
}
