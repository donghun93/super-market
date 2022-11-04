package com.devwinter.supermarket.config.security.data;

import com.devwinter.supermarket.admin.role.command.domain.MemberRole;
import com.devwinter.supermarket.admin.role.command.domain.MemberRoleRepository;
import com.devwinter.supermarket.admin.role.command.domain.Role;
import com.devwinter.supermarket.admin.role.command.domain.RoleRepository;
import com.devwinter.supermarket.member.command.domain.MemberEmail;
import com.devwinter.supermarket.member.command.domain.Member;
import com.devwinter.supermarket.member.command.domain.MemberRepository;
import com.devwinter.supermarket.member.command.domain.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("local")
public class InitDataRunner implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        Member admin = Member.builder()
                .pass(new Password(passwordEncoder.encode("1111!")))
                .name("관리자")
                .email(new MemberEmail("admin@devwinter.com"))
                .build();
        memberRepository.save(admin);

        List<Role> roles = roleRepository.findAll();
//        for (Role role : roles) {
//            memberRoleRepository.save(MemberRole.builder()
//                    .member(admin)
//                    .role(role)
//                    .build());
//        }

        Role role = roles.stream().filter(r -> r.getName().equals("ROLE_ADMIN"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("관리자 권한이 없습니다."));

        memberRoleRepository.save(MemberRole.builder()
                .member(admin)
                .role(role)
                .build());
    }
}
