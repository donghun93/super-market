package com.devwinter.supermarket.admin.member.infra;

import com.devwinter.supermarket.admin.member.command.request.MemberRoleStatus;
import com.devwinter.supermarket.admin.member.query.repository.AdminMemberQueryRepository;
import com.devwinter.supermarket.admin.member.query.response.AdminMemberDetailResponse;
import com.devwinter.supermarket.admin.member.query.response.AdminMemberListItemResponse;
import com.devwinter.supermarket.admin.role.command.domain.QMemberRole;
import com.devwinter.supermarket.admin.role.command.domain.Role;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.devwinter.supermarket.admin.role.command.domain.QMemberRole.memberRole;
import static com.devwinter.supermarket.admin.role.command.domain.QRole.role;
import static com.devwinter.supermarket.member.command.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class AdminMemberQueryRepositoryImpl implements AdminMemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminMemberListItemResponse> getAdminMemberList(Pageable pageable) {
        List<AdminMemberListItemResponse> contents = queryFactory
                .select(Projections.constructor(AdminMemberListItemResponse.class,
                        member.id, member.email.value, member.name, member.gender, member.deleteYn, member.blockYn, member.mannerPoint.value, member.createdDate, role.name, role.desc))
                .from(memberRole)
                .join(memberRole.member, member)
                .join(memberRole.role, role)
                .orderBy(member.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(member);

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<AdminMemberDetailResponse> getAdminMemberDetail(Long id) {
        AdminMemberDetailResponse adminMemberDetailResponse = queryFactory
                .select(Projections.constructor(AdminMemberDetailResponse.class,
                        memberRole.id, member.email.value, member.name,
                        member.address.address, member.address.detail, member.address.zipcode,
                        member.gender, member.mannerPoint.value, member.deleteYn, member.blockYn, member.createdDate, member.blockDate))
                .from(memberRole)
                .join(memberRole.member, member)
                .where(memberRole.member.id.eq(id))
                .fetchOne();

        MemberRoleStatus memberRoleStatus = queryFactory
                .select(Projections.constructor(MemberRoleStatus.class,
                        role.id, role.name, role.desc))
                .from(memberRole)
                .join(memberRole.member, member)
                .join(memberRole.role, role)
                .where(member.id.eq(id))
                .fetchOne();

        if(adminMemberDetailResponse != null) {
            adminMemberDetailResponse.setRole(memberRoleStatus);
        }

        return Optional.ofNullable(adminMemberDetailResponse);
    }
}
