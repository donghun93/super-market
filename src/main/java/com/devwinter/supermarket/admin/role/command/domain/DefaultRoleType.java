package com.devwinter.supermarket.admin.role.command.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DefaultRoleType {
    ROLE_ADMIN("관리자"),
    ROLE_SYS("시스템 관리자"),
    ROLE_USER("사용자")
    ;
    
    private final String desc;
}
