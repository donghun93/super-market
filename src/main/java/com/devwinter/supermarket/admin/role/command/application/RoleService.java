package com.devwinter.supermarket.admin.role.command.application;

import com.devwinter.supermarket.admin.role.command.application.request.RoleCreate;
import com.devwinter.supermarket.admin.role.command.application.request.RoleUpdate;
import com.devwinter.supermarket.member.command.domain.Member;

public interface RoleService {
    Long createRole(RoleCreate roleCreate);
    void deleteRole(Long roleId);
    void updateRole(RoleUpdate roleUpdate);
    void setMemberDefaultUserRole(Member member);
}
