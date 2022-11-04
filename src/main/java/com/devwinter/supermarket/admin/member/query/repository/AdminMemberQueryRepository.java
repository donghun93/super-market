package com.devwinter.supermarket.admin.member.query.repository;

import com.devwinter.supermarket.admin.member.query.response.AdminMemberDetailResponse;
import com.devwinter.supermarket.admin.member.query.response.AdminMemberListItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AdminMemberQueryRepository {
    Page<AdminMemberListItemResponse> getAdminMemberList(Pageable pageable);
    Optional<AdminMemberDetailResponse> getAdminMemberDetail(Long id);
}
