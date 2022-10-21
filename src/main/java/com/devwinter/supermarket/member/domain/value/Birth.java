package com.devwinter.supermarket.member.domain.value;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Birth {
    @Column(name = "birth_year")
    private String year;
    @Column(name = "birth_month")
    private String month;
    @Column(name = "birth_day")
    private String day;

    @Builder
    private Birth(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
