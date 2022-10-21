package com.devwinter.supermarket.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegionErrorCode {

    REGION_MAX_OVER("설정 동네를 최대 2개까지만 설정가능합니다."),
    REGION_NOT_FOUND("동네를 찾을 수 없습니다.")
    ;

    private final String description;
}
