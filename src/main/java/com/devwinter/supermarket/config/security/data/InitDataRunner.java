package com.devwinter.supermarket.config.security.data;

import com.devwinter.supermarket.admin.role.command.domain.MemberRole;
import com.devwinter.supermarket.admin.role.command.domain.MemberRoleRepository;
import com.devwinter.supermarket.admin.role.command.domain.Role;
import com.devwinter.supermarket.admin.role.command.domain.RoleRepository;
import com.devwinter.supermarket.member.command.domain.Email;
import com.devwinter.supermarket.member.command.domain.Member;
import com.devwinter.supermarket.member.command.domain.MemberRepository;
import com.devwinter.supermarket.member.command.domain.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDataRunner implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        Member admin = Member.builder()
                .pass(new Password(passwordEncoder.encode("1111")))
                .name("관리자")
                .email(new Email("admin@devwinter.com"))
                .build();
        memberRepository.save(admin);

        List<Role> roles = roleRepository.findAll();
        for (Role role : roles) {
            memberRoleRepository.save(MemberRole.builder()
                    .member(admin)
                    .role(role)
                    .build());
        }
    }
}
