package com.devwinter.supermarket.admin.role.command.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleErrorCode {
    ROLE_NOT_FOUND("권한이 존재하지 않습니다."),
    ROLE_USED_NOT_DELETE("권한이 사용중이여서 삭제할 수 없습니다."),
    ROLE_DUPLICATE_NAME("동일한 권한이 이미 존재합니다.")

    ;

    private final String description;
}
