package com.devwinter.supermarket.admin.member.query.application;

import com.devwinter.supermarket.admin.member.command.request.MemberRoleStatus;
import com.devwinter.supermarket.admin.member.query.response.AdminMemberDetailResponse;
import com.devwinter.supermarket.admin.member.query.response.AdminMemberListItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface AdminMemberQueryService {

    Page<AdminMemberListItemResponse> getMemberList(Pageable pageable);
    AdminMemberDetailResponse getMemberDetail(Long memberId);

    List<MemberRoleStatus> getRoles();

}
