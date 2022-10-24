package com.devwinter.supermarket.admin.repository.impl;

import com.devwinter.supermarket.admin.repository.NoticeQueryRepository;
import com.devwinter.supermarket.admin.response.NoticeDetailResponse;
import com.devwinter.supermarket.admin.response.NoticeSimpleResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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

import static com.devwinter.supermarket.admin.domain.QNotice.notice;
import static com.devwinter.supermarket.member.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class NoticeQueryRepositoryImpl implements NoticeQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<NoticeSimpleResponse> getNoticeAll(Pageable pageable) {
        List<NoticeSimpleResponse> notices = queryFactory
                .select(Projections.constructor(NoticeSimpleResponse.class,
                        notice.id, notice.title, member.personalInformation.name, notice.showYn, notice.deleteYn, notice.createdDate, notice.modifiedDate)
                )
                .from(notice)
                .join(notice.member, member)
                .where(isActive())
                .orderBy(notice.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(notice)
                .where(isActive());


        return PageableExecutionUtils.getPage(notices,
                pageable,
                countQuery::fetchOne);
    }

    @Override
    public Optional<NoticeDetailResponse> getNoticeDetail(Long noticeId) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(NoticeDetailResponse.class, notice.id, notice.title, notice.content,
                        member.personalInformation.name, notice.createdDate))
                .from(notice)
                .join(notice.member, member)
                .where(notice.id.eq(noticeId), isActive())
                .fetchOne());
    }

    private BooleanExpression isVisible() {
        return notice.showYn.eq(true);
    }

    private BooleanExpression isNotDelete() {
        return notice.deleteYn.eq(false);
    }

    private BooleanExpression isActive() {
        return isVisible().and(isNotDelete());
    }
}
