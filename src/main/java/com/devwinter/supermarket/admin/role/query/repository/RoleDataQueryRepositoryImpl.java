package com.devwinter.supermarket.admin.role.query.repository;

import com.devwinter.supermarket.admin.role.query.response.RoleDetailItemResponse;
import com.devwinter.supermarket.admin.role.query.response.RoleListItemResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.devwinter.supermarket.admin.role.command.domain.QRole.role;
import static com.querydsl.core.types.dsl.Wildcard.count;

@Repository
@RequiredArgsConstructor
public class RoleDataQueryRepositoryImpl implements RoleDataQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RoleListItemResponse> getRoleList(Pageable pageable) {
        List<RoleListItemResponse> fetch = queryFactory
                .select(Projections.constructor(RoleListItemResponse.class, role.id, role.name, role.desc))
                .from(role)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(count)
                .from(role);

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<RoleDetailItemResponse> getRoleDetail(Long roleId) {

        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(RoleDetailItemResponse.class,
                        role.id, role.name, role.desc, role.createdBy, role.createdDate))
                .from(role)
                .where(role.id.eq(roleId))
                .fetchOne());
    }
}
