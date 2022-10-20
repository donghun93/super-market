package com.devwinter.supermarket.member.repository;

import com.devwinter.supermarket.member.domain.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<Member> findDuplicateMember(String phoneNumber, String email);
}
