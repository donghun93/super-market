package com.devwinter.supermarket.member.command.application.impl;

import com.devwinter.supermarket.admin.role.command.application.RoleService;
import com.devwinter.supermarket.admin.role.command.domain.MemberRoleRepository;
import com.devwinter.supermarket.member.command.application.MemberService;
import com.devwinter.supermarket.member.command.exception.MemberException;
import com.devwinter.supermarket.member.command.application.request.MemberCreate;
import com.devwinter.supermarket.member.command.application.request.RegionCreate;
import com.devwinter.supermarket.member.command.domain.MemberEmail;
import com.devwinter.supermarket.member.command.domain.Member;
import com.devwinter.supermarket.member.command.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.devwinter.supermarket.member.command.exception.MemberErrorCode.MEMBER_DUPLICATE_ERROR;
import static com.devwinter.supermarket.member.command.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    @Transactional
    public Long createMember(MemberCreate memberCreate) {
        memberDuplicateValid(memberCreate.getEmail());
        Member member = memberCreate.toEntity(passwordEncoder.encode(memberCreate.getPassword()));
        memberRepository.save(member);
        roleService.setMemberDefaultUserRole(member);
        return member.getId();
    }

    @Override
    @Transactional
    public void createRegion(Long memberId, RegionCreate regionCreate) {
        getMemberWithRegion(memberId).createRegion(regionCreate.toValue());
    }

    @Override
    @Transactional
    public void regionAuth(Long memberId, int regionIdx) {
        getMemberWithRegion(memberId).regionAuthProcess(regionIdx);
    }

    @Override
    @Transactional
    public void changeLeadRegion(Long memberId, int regionIdx) {
        getMemberWithRegion(memberId).changeLeadRegion(regionIdx);
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId) {
        getMember(memberId).deleteMember();
    }

    @Override
    @Transactional
    public void deleteRegion(Long memberId, int regionIdx) {
        getMember(memberId).deleteRegion(regionIdx);
    }

    @Override
    @Transactional
    public void memberBlock(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        member.changeBlock();
    }

    private Member getMemberWithRegion(Long memberId) {
        return memberRepository.findWithRegionById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    private void memberDuplicateValid(String email) {
        memberRepository.findByEmail(new MemberEmail(email))
                .ifPresent(m -> {
                    throw new MemberException(MEMBER_DUPLICATE_ERROR);
                });
    }
}
