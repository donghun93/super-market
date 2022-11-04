package com.devwinter.supermarket.admin.member.command.application;

import com.devwinter.supermarket.admin.role.command.domain.MemberRole;
import com.devwinter.supermarket.admin.role.command.domain.MemberRoleRepository;
import com.devwinter.supermarket.admin.role.command.domain.Role;
import com.devwinter.supermarket.admin.role.command.domain.RoleRepository;
import com.devwinter.supermarket.admin.role.command.exception.RoleErrorCode;
import com.devwinter.supermarket.admin.role.command.exception.RoleException;
import com.devwinter.supermarket.member.command.application.MemberService;
import com.devwinter.supermarket.member.command.domain.Member;
import com.devwinter.supermarket.member.command.domain.MemberRepository;
import com.devwinter.supermarket.member.command.exception.MemberErrorCode;
import com.devwinter.supermarket.member.command.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.devwinter.supermarket.admin.role.command.exception.RoleErrorCode.MEMBER_ROLE_NOT_MATCHED;
import static com.devwinter.supermarket.admin.role.command.exception.RoleErrorCode.ROLE_NOT_FOUND;
import static com.devwinter.supermarket.member.command.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberRoleService {

    private final RoleRepository roleRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void changeMemberRole(Long memberRoleId, Long roleId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleException(ROLE_NOT_FOUND));

        MemberRole memberRole = memberRoleRepository.findById(memberRoleId)
                .orElseThrow(() -> new RoleException(MEMBER_ROLE_NOT_MATCHED));
        memberRole.changeRole(role);
    }
}
