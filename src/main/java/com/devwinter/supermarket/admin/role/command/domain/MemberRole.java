package com.devwinter.supermarket.admin.role.command.domain;

import com.devwinter.supermarket.member.command.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_role_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    private MemberRole(Role role, Member member) {
        this.role = role;
        this.member = member;
    }
}
