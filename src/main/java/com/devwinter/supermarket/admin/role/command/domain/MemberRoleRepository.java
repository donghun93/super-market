package com.devwinter.supermarket.admin.role.command.domain;

import com.devwinter.supermarket.member.command.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {

    @Query("select mr.role.name from MemberRole mr join mr.role where mr.member = :member")
    Set<String> findByUserRolesByMember(@Param("member") Member member);

    @Query("select count(mr) from MemberRole mr where mr.role = :role")
    Long countByRole(@Param("role") Role role);
}
