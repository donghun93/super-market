package com.devwinter.supermarket.member.command.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode {
    MEMBER_DUPLICATE_ERROR("이미 회원이 존재합니다."),
    MEMBER_NOT_FOUND("회원이 존재하지 않습니다."),
    MEMBER_REGION_MAX("회원 동네 생성은 최대 2개입니다."),
    MEMBER_ALREADY_REGION_REGISTER("동일한 위치의 동네가 존재합니다."),
    MEMBER_ALREADY_REGION_NAME("동일한 동네이름이 존재합니다."),
    MEMBER_REGION_NOT_FOUND("동네가 존재하지 않습니다."),
    MEMBER_REGION_IDX_NOT_VALID("잘못된 동네 요청입니다.")
    ;

    private final String description;
}
