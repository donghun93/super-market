package com.devwinter.supermarket.member.command.application;

import com.devwinter.supermarket.member.command.application.request.MemberCreate;
import com.devwinter.supermarket.member.command.application.request.RegionCreate;

public interface MemberService {
    Long createMember(MemberCreate memberCreate);
    void createRegion(Long memberId, RegionCreate regionCreate);
    void regionAuth(Long memberId, int regionIdx);
    void changeLeadRegion(Long memberId, int regionIdx);
    void deleteMember(Long memberId);
    void deleteRegion(Long memberId, int regionIdx);
}
