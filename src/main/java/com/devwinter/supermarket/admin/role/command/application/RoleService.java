package com.devwinter.supermarket.admin.role.command.application;

import com.devwinter.supermarket.admin.role.command.application.request.RoleCreate;
import com.devwinter.supermarket.admin.role.command.application.request.RoleUpdate;

public interface RoleService {
    Long createRole(RoleCreate roleCreate);
    void deleteRole(Long roleId);
    void updateRole(RoleUpdate roleUpdate);
}
