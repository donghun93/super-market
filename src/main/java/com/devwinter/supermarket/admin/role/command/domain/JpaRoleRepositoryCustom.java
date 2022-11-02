package com.devwinter.supermarket.admin.role.command.domain;

import java.util.Optional;

public interface JpaRoleRepositoryCustom {
    Optional<Role> findRoleWithChildById(Long roleId);
}
