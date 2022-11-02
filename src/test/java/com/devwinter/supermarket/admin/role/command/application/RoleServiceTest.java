package com.devwinter.supermarket.admin.role.command.application;

import com.devwinter.supermarket.admin.role.command.application.impl.RoleServiceImpl;
import com.devwinter.supermarket.admin.role.command.application.request.RoleCreate;
import com.devwinter.supermarket.admin.role.command.application.request.RoleUpdate;
import com.devwinter.supermarket.admin.role.command.domain.MemberRoleRepository;
import com.devwinter.supermarket.admin.role.command.domain.Role;
import com.devwinter.supermarket.admin.role.command.domain.RoleRepository;
import com.devwinter.supermarket.admin.role.command.exception.RoleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.devwinter.supermarket.admin.role.command.exception.RoleErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private MemberRoleRepository memberRoleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    @DisplayName("권한 추가 시 동일한 권한이 존재할 경우 테스트")
    void createRoleDuplicateRoleNameTest() {
        // given
        RoleCreate roleCreate = RoleCreate.builder()
                .roleName("ROLE_ADMIN")
                .roleDesc("관리자")
                .build();

        given(roleRepository.findByNameOrDesc(anyString(), anyString()))
                .willReturn(Optional.of(Role.builder().build()));

        // when
        RoleException roleException = assertThrows(RoleException.class,
                () -> roleService.createRole(roleCreate));

        // then
        verify(roleRepository, times(1)).findByNameOrDesc(anyString(), anyString());
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
        ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);

        // when
        roleService.createRole(roleCreate);

        // then
        verify(roleRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("ROLE_ADMIN");
        assertThat(captor.getValue().getDesc()).isEqualTo("관리자");
    }

    @Test
    @DisplayName("권한 삭제 시 권한이 없을 경우 테스트")
    void deleteRoleNotFoundTest() {
        // given
        given(roleRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        RoleException roleException = assertThrows(RoleException.class,
                () -> roleService.deleteRole(anyLong()));

        // then
        verify(roleRepository, times(1)).findById(anyLong());
        assertThat(roleException.getErrorCode()).isEqualTo(ROLE_NOT_FOUND.toString());
        assertThat(roleException.getErrorMessage()).isEqualTo(ROLE_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("권한 삭제 시 사용중이면 삭제못하는 테스트")
    void deleteRoleChildExistNotDeleteTest() {
        // given
        given(roleRepository.findById(anyLong()))
                .willReturn(Optional.of(Role.builder().build()));

        given(memberRoleRepository.countByRole(any()))
                .willReturn(1L);

        // when
        RoleException roleException = assertThrows(RoleException.class,
                () -> roleService.deleteRole(anyLong()));

        // then
        verify(roleRepository, times(1)).findById(anyLong());
        verify(memberRoleRepository, times(1)).countByRole(any());
        assertThat(roleException.getErrorCode()).isEqualTo(ROLE_USED_NOT_DELETE.toString());
        assertThat(roleException.getErrorMessage()).isEqualTo(ROLE_USED_NOT_DELETE.getDescription());
    }

    @Test
    @DisplayName("권한 삭제 테스트")
    void deleteRoleTest() {
        // given
        Role role = Role.builder()
                .name("ROLE_ADMIN")
                .desc("관리자")
                .build();
        given(roleRepository.findById(anyLong()))
                .willReturn(Optional.of(role));

        given(memberRoleRepository.countByRole(any()))
                .willReturn(0L);

        ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);

        // when
        roleService.deleteRole(anyLong());

        // then
        verify(roleRepository, times(1)).findById(anyLong());
        verify(memberRoleRepository, times(1)).countByRole(any());
        verify(roleRepository, times(1)).delete(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("ROLE_ADMIN");
        assertThat(captor.getValue().getDesc()).isEqualTo("관리자");
    }

    @Test
    @DisplayName("권한 변경시 동일한 이름이 존재할 경우")
    void changeRoleDuplicateNameOrDescTest() {
        // given
        RoleUpdate roleUpdate = RoleUpdate.builder()
                .id(1L)
                .name("ROLE_ADMIN")
                .desc("관리자")
                .build();

        given(roleRepository.findByIdIsNotAndNameOrDesc(anyLong(), anyString(), anyString()))
                .willReturn(Optional.of(Role.builder().build()));

        // when
        RoleException roleException = assertThrows(RoleException.class,
                () -> roleService.updateRole(roleUpdate));

        // then
        verify(roleRepository, times(1)).findByIdIsNotAndNameOrDesc(anyLong(), anyString(), anyString());
        assertThat(roleException.getErrorCode()).isEqualTo(ROLE_DUPLICATE_NAME.toString());
        assertThat(roleException.getErrorMessage()).isEqualTo(ROLE_DUPLICATE_NAME.getDescription());
    }

    @Test
    @DisplayName("권한 변경시 권한이 존재하지 않을경우")
    void changeRoleNotFoundRoleTest() {
        // given
        RoleUpdate roleUpdate = RoleUpdate.builder()
                .id(1L)
                .name("ROLE_ADMIN")
                .desc("관리자")
                .build();

        given(roleRepository.findByIdIsNotAndNameOrDesc(anyLong(), anyString(), anyString()))
                .willReturn(Optional.empty());
        given(roleRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        RoleException roleException = assertThrows(RoleException.class,
                () -> roleService.updateRole(roleUpdate));

        // then
        verify(roleRepository, times(1)).findByIdIsNotAndNameOrDesc(anyLong(), anyString(), anyString());
        verify(roleRepository, times(1)).findById(anyLong());
        assertThat(roleException.getErrorCode()).isEqualTo(ROLE_NOT_FOUND.toString());
        assertThat(roleException.getErrorMessage()).isEqualTo(ROLE_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("권한 변경시 테스트")
    void changeRoleTest() {
        // given
        RoleUpdate roleUpdate = RoleUpdate.builder()
                .id(1L)
                .name("ROLE_ADMIN")
                .desc("관리자")
                .build();
        
        Role role = Role.builder()
                .name("ROLE_USER")
                .desc("사용자")
                .build();

        Role mockRole = spy(role);

        given(roleRepository.findByIdIsNotAndNameOrDesc(anyLong(), anyString(), anyString()))
                .willReturn(Optional.empty());
        given(roleRepository.findById(anyLong()))
                .willReturn(Optional.of(mockRole));

        // when
        roleService.updateRole(roleUpdate);

        // then
        verify(roleRepository, times(1)).findByIdIsNotAndNameOrDesc(anyLong(),anyString(), anyString());
        verify(roleRepository, times(1)).findById(anyLong());
        verify(mockRole, times(1)).changeRole(roleUpdate.getName(), roleUpdate.getDesc());
        assertThat(mockRole.getName()).isEqualTo("ROLE_ADMIN");
        assertThat(mockRole.getDesc()).isEqualTo("관리자");
    }
}