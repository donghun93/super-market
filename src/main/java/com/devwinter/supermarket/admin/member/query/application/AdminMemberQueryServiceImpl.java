package com.devwinter.supermarket.admin.member.query.application;

import com.devwinter.supermarket.admin.member.command.request.MemberRoleStatus;
import com.devwinter.supermarket.admin.member.query.repository.AdminMemberQueryRepository;
import com.devwinter.supermarket.admin.member.query.response.AdminMemberDetailResponse;
import com.devwinter.supermarket.admin.member.query.response.AdminMemberListItemResponse;
import com.devwinter.supermarket.admin.role.command.domain.RoleRepository;
import com.devwinter.supermarket.admin.role.query.application.RoleQueryService;
import com.devwinter.supermarket.admin.role.query.repository.RoleDataQueryRepository;
import com.devwinter.supermarket.member.command.exception.MemberErrorCode;
import com.devwinter.supermarket.member.command.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMemberQueryServiceImpl implements AdminMemberQueryService {
    private final AdminMemberQueryRepository adminMemberQueryRepository;
    private final RoleRepository roleRepository;

    @Override
    public Page<AdminMemberListItemResponse> getMemberList(Pageable pageable) {
        return adminMemberQueryRepository.getAdminMemberList(pageable);
    }

    @Override
    public AdminMemberDetailResponse getMemberDetail(Long memberId) {
        return adminMemberQueryRepository.getAdminMemberDetail(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public List<MemberRoleStatus> getRoles() {
        return roleRepository.findAll().stream()
                .map(r -> new MemberRoleStatus(r.getId(), r.getName(), r.getDesc()))
                .collect(Collectors.toList());
    }
}
