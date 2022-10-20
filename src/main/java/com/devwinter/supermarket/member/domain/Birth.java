package com.devwinter.supermarket.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;


@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Birth {
    private String year;
    private String month;
    private String day;

    @Builder
    private Birth(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
