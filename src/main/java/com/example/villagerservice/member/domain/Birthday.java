package com.example.villagerservice.member.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Birthday {
    @Column(name = "birth_year", length = 4)
    private int year;
    @Column(name = "birth_month", length = 2)
    private int month;
    @Column(name = "birth_day", length = 2)
    private int day;
    public Birthday(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getBirth() {
        return String.format("%d-%02d-%02d", this.year, this.month, this.day);
    }
}
