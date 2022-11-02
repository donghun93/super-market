package com.devwinter.supermarket.admin.role.command.application;

import com.devwinter.supermarket.admin.role.command.application.request.RoleCreate;
import com.devwinter.supermarket.admin.role.command.application.request.RoleUpdate;
import com.devwinter.supermarket.admin.role.command.domain.MemberRole;
import com.devwinter.supermarket.admin.role.command.domain.MemberRoleRepository;
import com.devwinter.supermarket.admin.role.command.domain.Role;
import com.devwinter.supermarket.admin.role.command.domain.RoleRepository;
import com.devwinter.supermarket.admin.role.command.exception.RoleException;
import com.devwinter.supermarket.member.command.domain.Email;
import com.devwinter.supermarket.member.command.domain.Member;
import com.devwinter.supermarket.member.command.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.devwinter.supermarket.admin.role.command.exception.RoleErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles({"test"})
public class RoleServiceIntegratedTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MemberRoleRepository memberRoleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("권한 추가 시 동일한 권한이 있을 경우 테스트 - name 비교")
    void createRoleDuplicateNameTest() {
        // given
        RoleCreate role1 = RoleCreate.builder()
                .roleName("ROLE_ADMIN")
                .roleDesc("관리자")
                .build();
        roleService.createRole(role1);

        RoleCreate role2 = RoleCreate.builder()
                .roleName("ROLE_ADMIN")
                .roleDesc("관리자2")
                .build();

        // when
        RoleException roleException = assertThrows(RoleException.class,
                () -> roleService.createRole(role2));

        // then
        assertThat(roleException.getErrorCode()).isEqualTo(ROLE_DUPLICATE_NAME.toString());
        assertThat(roleException.getErrorMessage()).isEqualTo(ROLE_DUPLICATE_NAME.getDescription());
    }

    @Test
    @DisplayName("권한 추가 시 동일한 권한이 있을 경우 테스트 - desc 비교")
    void createRoleDuplicateDescTest() {
        // given
        RoleCreate role1 = RoleCreate.builder()
                .roleName("ROLE_ADMIN")
                .roleDesc("관리자")
                .build();
        roleService.createRole(role1);

        RoleCreate role2 = RoleCreate.builder()
                .roleName("ROLE_ADMIN2")
                .roleDesc("관리자")
                .build();

        // when
        RoleException roleException = assertThrows(RoleException.class,
                () -> roleService.createRole(role2));

        // then
        assertThat(roleException.getErrorCode()).isEqualTo(ROLE_DUPLICATE_NAME.toString());
        assertThat(roleException.getErrorMessage()).isEqualTo(ROLE_DUPLICATE_NAME.getDescription());
    }

    @Test
    @DisplayName("권한 추가 시 동일한 권한이 있을 경우 테스트")
    void createRoleDuplicateNameAndDescTest() {
        // given
        RoleCreate role1 = RoleCreate.builder()
                .roleName("ROLE_ADMIN")
                .roleDesc("관리자")
                .build();
        roleService.createRole(role1);

        RoleCreate role2 = RoleCreate.builder()
                .roleName("ROLE_ADMIN")
                .roleDesc("관리자")
                .build();

        // when
        RoleException roleException = assertThrows(RoleException.class,
                () -> roleService.createRole(role2));

        // then
        assertThat(roleException.getErrorCode()).isEqualTo(ROLE_DUPLICATE_NAME.toString());
        assertThat(roleException.getErrorMessage()).isEqualTo(ROLE_DUPLICATE_NAME.getDescription());
    }

    @Test
    @DisplayName("권한 추가 테스트")
    void createParentNotInsertRole() {
        // given
        RoleCreate roleCreate = RoleCreate.builder()
                .roleName("ROLE_ADMIN")
                .roleDesc("관리자")
                .build();

        // when
        roleService.createRole(roleCreate);

        // then
        List<Role> lists = roleRepository.findAll();
        assertThat(lists.size()).isEqualTo(1);
        assertThat(lists.get(0).getName()).isEqualTo("ROLE_ADMIN");
        assertThat(lists.get(0).getDesc()).isEqualTo("관리자");
    }

    @Test
    @DisplayName("권한 삭제 시 사용할 경우 실패 테스트")
    void deleteRoleExistChildTest() {
        // given
        RoleCreate parent = RoleCreate.builder()
                .roleName("ROLE_ADMIN")
                .roleDesc("관리자")
                .build();
        Long roleId = roleService.createRole(parent);

        Member member = Member.builder()
                .email(new Email("test"))
                .build();

        memberRepository.save(member);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleException(ROLE_NOT_FOUND));

        memberRoleRepository.save(MemberRole.builder()
                .member(member)
                .role(role)
                .build());

        // when
        RoleException roleException = assertThrows(RoleException.class,
                () -> roleService.deleteRole(roleId));

        // then
        assertThat(roleException.getErrorCode()).isEqualTo(ROLE_USED_NOT_DELETE.toString());
        assertThat(roleException.getErrorMessage()).isEqualTo(ROLE_USED_NOT_DELETE.getDescription());
    }

    @Test
    @DisplayName("권한 삭제 테스트")
    void deleteRoleTest() {
        // given
        RoleCreate parent = RoleCreate.builder()
                .roleName("ROLE_ADMIN")
                .roleDesc("관리자")
                .build();
        Long roleId = roleService.createRole(parent);

        // when
        roleService.deleteRole(roleId);

        // then
        long totalCount = roleRepository.count();
        assertThat(totalCount).isEqualTo(0);
    }

    @Test
    @DisplayName("권한 - 이름과 설명 변경 테스트")
    void changeNameAndDescRoleTest() {
        // given
        Role role = Role.builder()
                .name("ROLE_ADMIN")
                .desc("관리자")
                .build();
        roleRepository.save(role);

        RoleUpdate roleUpdate = RoleUpdate.builder()
                .id(role.getId())
                .name("ROLE_SYS")
                .desc("시스템")
                .build();

        // when
        roleService.updateRole(roleUpdate);

        // then
        assertThat(role.getName()).isEqualTo("ROLE_SYS");
        assertThat(role.getDesc()).isEqualTo("시스템");
    }
}
