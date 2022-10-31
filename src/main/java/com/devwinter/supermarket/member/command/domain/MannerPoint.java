package com.devwinter.supermarket.member.command.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MannerPoint {

    @Column(name = "manner_point")
    private int value;

    public MannerPoint(int value) {
        this.value = value;
    }
}
