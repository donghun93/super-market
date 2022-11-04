package com.devwinter.supermarket.member.command.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MAN("남"),
    WOMAN("여")
    ;

    private final String value;
}
