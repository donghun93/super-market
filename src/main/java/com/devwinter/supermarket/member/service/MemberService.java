package com.devwinter.supermarket.member.service;

import com.devwinter.supermarket.member.request.MemberCreateRequest;
import com.devwinter.supermarket.member.response.MemberDetailResponse;
import com.devwinter.supermarket.member.response.MemberListResponse;

public interface MemberService {
    void createMember(MemberCreateRequest memberCreateRequest);
    MemberDetailResponse getMember(String memberEmail);
    MemberListResponse getMemberList();
    void resignMember(Long memberId);
}
