package com.devwinter.supermarket.member.service.impl;

import com.devwinter.supermarket.member.domain.Member;
import com.devwinter.supermarket.member.exception.MemberException;
import com.devwinter.supermarket.member.repository.MemberRepository;
import com.devwinter.supermarket.member.request.MemberCreateRequest;
import com.devwinter.supermarket.member.response.MemberDetailResponse;
import com.devwinter.supermarket.member.response.MemberListResponse;
import com.devwinter.supermarket.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.devwinter.supermarket.member.exception.MemberErrorCode.MEMBER_ALREADY_EXIST;
import static com.devwinter.supermarket.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void createMember(MemberCreateRequest memberCreateRequest) {
        memberValidate(memberCreateRequest);
        memberRepository.save(memberCreateRequest.toEntity());
    }

    private void memberValidate(MemberCreateRequest memberCreateRequest) {
        // 이메일, 폰번호 중복 검사
        memberRepository.findDuplicateMember(
                memberCreateRequest.getPhoneNumber(),
                memberCreateRequest.getEmail())
                .ifPresent(m -> {
                    throw new MemberException(MEMBER_ALREADY_EXIST);
                });
    }

    @Override
    public MemberDetailResponse getMember(String memberEmail) {
        return MemberDetailResponse.of(memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND)));
    }

    @Override
    public MemberListResponse getMemberList() {
        return MemberListResponse.of(memberRepository.findAll());
    }

    @Override
    @Transactional
    public void resignMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        member.resign();
    }
}
