package com.devwinter.supermarket.admin.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryErrorCode {

    CATEGORY_NOT_FOUND ("카테고리가 존재하지 않습니다."),
    CATEGORY_ALREADY_EXIST("동일한 카테고리가 존재합니다.")
    ;

    private final String description;
}
