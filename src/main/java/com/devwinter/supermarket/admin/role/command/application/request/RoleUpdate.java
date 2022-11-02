package com.devwinter.supermarket.admin.role.command.application.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class RoleUpdate {
    private Long id;

    @Pattern(regexp = "^ROLE_.*", message = "권한명은 ROLE_로 시작해야합니다.")
    private String name;

    @NotBlank(message = "권한설명은 필수값입니다.")
    private String desc;

    private String createdBy;
    private String createDate;

    @Builder
    private RoleUpdate(Long id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }
}
