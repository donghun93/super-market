package com.devwinter.supermarket.admin.role.query.repository;

import com.devwinter.supermarket.admin.role.query.response.RoleDetailItemResponse;
import com.devwinter.supermarket.admin.role.query.response.RoleListItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RoleDataQueryRepository {
    Page<RoleListItemResponse> getRoleList(Pageable pageable);
    Optional<RoleDetailItemResponse> getRoleDetail(Long roleId);
}
