package com.devwinter.supermarket.admin.role.command.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>, RoleCustomRepository {

    Optional<Role> findByNameOrDesc(String name, String desc);
}
