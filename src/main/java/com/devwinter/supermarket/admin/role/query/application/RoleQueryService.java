package com.devwinter.supermarket.admin.role.query.application;

import com.devwinter.supermarket.admin.role.command.exception.RoleErrorCode;
import com.devwinter.supermarket.admin.role.command.exception.RoleException;
import com.devwinter.supermarket.admin.role.query.dao.RoleDataQueryRepository;
import com.devwinter.supermarket.admin.role.query.response.RoleDetailItemResponse;
import com.devwinter.supermarket.admin.role.query.response.RoleListItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleQueryService {

    private final RoleDataQueryRepository roleDataQueryRepository;

    public Page<RoleListItemResponse> getRoleList(Pageable page) {
        return roleDataQueryRepository.getRoleList(page);
    }

    public RoleDetailItemResponse getRoleDetail(Long roleId) {
        return roleDataQueryRepository.getRoleDetail(roleId)
                .orElseThrow(() -> new RoleException(RoleErrorCode.ROLE_NOT_FOUND));
    }
}
