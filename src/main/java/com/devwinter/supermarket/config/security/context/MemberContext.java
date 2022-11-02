package com.devwinter.supermarket.config.security.context;

import com.devwinter.supermarket.member.command.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class MemberContext extends User {

    private final Member member;

    public MemberContext(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getEmail().getValue(), member.getPass().getValue(), authorities);
        this.member = member;
    }
}
