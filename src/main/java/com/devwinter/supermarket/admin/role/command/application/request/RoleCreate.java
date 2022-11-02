package com.devwinter.supermarket.admin.role.command.application.request;

import com.devwinter.supermarket.admin.role.command.domain.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class RoleCreate {

    @Pattern(regexp = "^ROLE_.*", message = "권한명은 ROLE_로 시작해야합니다.")
    private String roleName; // 패턴 필요할듯

    @NotBlank(message = "권한설명은 필수값입니다.")
    private String roleDesc;

    @Builder
    private RoleCreate(String roleName, String roleDesc) {
        this.roleName = roleName;
        this.roleDesc = roleDesc;
    }

    public Role toEntity() {
        return Role.builder()
                .name(roleName)
                .desc(roleDesc)
                .build();
    }
}
