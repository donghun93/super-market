package com.devwinter.supermarket.member.repository.impl;

import com.devwinter.supermarket.member.domain.Member;
import com.devwinter.supermarket.member.domain.QMember;
import com.devwinter.supermarket.member.repository.MemberRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.devwinter.supermarket.member.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Member> findDuplicateMember(String phoneNumber, String email) {
        return Optional.ofNullable(jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.email.eq(email).or(
                        member.personalInformation.phoneNumber.eq(phoneNumber)))
                .fetchOne());
    }
}
