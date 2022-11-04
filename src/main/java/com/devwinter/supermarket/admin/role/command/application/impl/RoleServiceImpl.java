package com.devwinter.supermarket.admin.role.command.application.impl;

import com.devwinter.supermarket.admin.role.command.application.RoleService;
import com.devwinter.supermarket.admin.role.command.application.request.RoleCreate;
import com.devwinter.supermarket.admin.role.command.application.request.RoleUpdate;
import com.devwinter.supermarket.admin.role.command.domain.*;
import com.devwinter.supermarket.admin.role.command.exception.RoleException;
import com.devwinter.supermarket.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.devwinter.supermarket.admin.role.command.domain.DefaultRoleType.ROLE_USER;
import static com.devwinter.supermarket.admin.role.command.exception.RoleErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final MemberRoleRepository memberRoleRepository;

    @Override
    @Transactional
    public Long createRole(RoleCreate roleCreate) {
        roleDuplicateValid(roleCreate.getRoleName(), roleCreate.getRoleDesc());
        Role role = roleCreate.toEntity();
        roleRepository.save(role);
        return role.getId();
    }

    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleException(ROLE_NOT_FOUND));

        if (memberRoleRepository.countByRole(role) > 0) {
            throw new RoleException(ROLE_USED_NOT_DELETE);
        }
        roleRepository.delete(role);
    }

    @Override
    @Transactional
    public void updateRole(RoleUpdate roleUpdate) {
        roleDuplicateValid(roleUpdate.getId(), roleUpdate.getName(), roleUpdate.getDesc());
        Role role = roleRepository.findById(roleUpdate.getId())
                .orElseThrow(() -> new RoleException(ROLE_NOT_FOUND));

        role.changeRole(roleUpdate.getName(), roleUpdate.getDesc());
    }

    @Override
    @Transactional
    public void setMemberDefaultUserRole(Member member) {
        Role role = getRoleByName(ROLE_USER);
        MemberRole memberRole = createMemberRole(member, role);
        memberRoleRepository.save(memberRole);
    }

    private Role getRoleByName(DefaultRoleType roleType) {
        return roleRepository.findByName(roleType.toString())
                .orElseThrow(() -> new RoleException(ROLE_NOT_FOUND));
    }

    private void roleDuplicateValid(String roleName, String roleDesc) {
        roleRepository.findByNameOrDesc(roleName, roleDesc)
                .ifPresent(r -> {
                    throw new RoleException(ROLE_DUPLICATE_NAME);
                });
    }

    private void roleDuplicateValid(Long id, String roleName, String roleDesc) {
        roleRepository.findByIdIsNotAndNameOrDesc(id, roleName, roleDesc)
                .ifPresent(r -> {
                    throw new RoleException(ROLE_DUPLICATE_NAME);
                });
    }

    private MemberRole createMemberRole(Member member, Role role) {
        return MemberRole.builder()
                .member(member)
                .role(role)
                .build();
    }
}
