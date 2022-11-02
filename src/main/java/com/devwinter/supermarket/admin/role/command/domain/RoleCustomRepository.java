package com.devwinter.supermarket.admin.role.command.domain;

import java.util.Optional;

public interface RoleCustomRepository {
    Optional<Role> findByIdIsNotAndNameOrDesc(Long id, String name, String desc);
}
