package com.devwinter.supermarket.admin.role.query.dao;

import com.devwinter.supermarket.admin.role.query.response.RoleListItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleDataQueryRepository {
    Page<RoleListItemResponse> getRoleList(Pageable pageable);
}
