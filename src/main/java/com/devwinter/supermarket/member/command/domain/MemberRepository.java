package com.devwinter.supermarket.member.command.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(Email email);


    @EntityGraph(attributePaths = {"regions"})
    Optional<Member> findWithRegionById(Long memberId);
}
