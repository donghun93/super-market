package com.devwinter.supermarket.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode {


    MEMBER_ALREADY_EXIST("동일한 회원이 존재합니다."),
    MEMBER_NOT_FOUND("회원이 존재하지 않습니다.")
    ;

    private final String description;
}