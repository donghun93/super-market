package com.devwinter.supermarket.member.command.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEmail {

    @Column(name = "email")
    private String value;

    public MemberEmail(String value) {
        this.value = value;
    }
}
