package com.devwinter.supermarket.admin.role.command.application.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoleUpdate {
    private Long id;
    private String name;
    private String desc;

    @Builder
    private RoleUpdate(Long id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }
}
