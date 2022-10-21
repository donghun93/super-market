package com.devwinter.supermarket.member.service.impl;

import com.devwinter.supermarket.member.domain.Member;
import com.devwinter.supermarket.member.domain.Region;
import com.devwinter.supermarket.member.domain.value.RegionLocation;
import com.devwinter.supermarket.member.domain.type.RegionRange;
import com.devwinter.supermarket.member.exception.MemberException;
import com.devwinter.supermarket.member.exception.RegionException;
import com.devwinter.supermarket.member.repository.MemberRepository;
import com.devwinter.supermarket.member.repository.RegionRepository;
import com.devwinter.supermarket.member.request.RegionCreateRequest;
import com.devwinter.supermarket.member.response.RegionListResponse;
import com.devwinter.supermarket.member.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.devwinter.supermarket.member.exception.MemberErrorCode.*;
import static com.devwinter.supermarket.member.exception.RegionErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void registerRegion(RegionCreateRequest request) {
        Member member = findMemberAndValidate(request.getMemberId());

        List<Region> regions = regionRepository.findByMember(member);
        if(regions.size() >= 2) {
            throw new RegionException(REGION_MAX_OVER);
        }

        Region region = Region.builder()
                .name(request.getName())
                .member(member)
                .location(RegionLocation.builder()
                        .latitude(request.getLatitude())
                        .longitude(request.getLongitude())
                        .build())
                .build();
        regionRepository.save(region);
    }

    @Override
    @Transactional
    public void setLeadRegion(Long regionId) {
        Region region = findRegionAndMemberValidate(regionId);
        setMemberRelationRegionAllFalse(region.getMember());

        region.changeLead(true);
    }

    @Override
    @Transactional
    public void authRegion(Long regionId) {
        Region findRegion = findRegionAndMemberValidate(regionId);
        findRegion.authSuccess();
    }

    @Override
    @Transactional
    public void setRegionRange(Long regionId, RegionRange range) {
        Region findRegion = findRegionAndMemberValidate(regionId);
        findRegion.changeRange(range);
    }

    @Override
    @Transactional
    public void deleteRegion(Long regionId) {
        Region findRegion = findRegionAndMemberValidate(regionId);
        regionRepository.delete(findRegion);
    }

    @Override
    public RegionListResponse getRegionList(Long memberId) {
        Member findMember = findMemberAndValidate(memberId);
        return RegionListResponse.of(regionRepository.findByMember(findMember));
    }

    private Member findMemberAndValidate(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        memberValidate(member);
        return member;
    }

    private void setMemberRelationRegionAllFalse(Member findMember) {
        List<Region> regions = regionRepository.findByMember(findMember);
        for (Region region : regions) {
            region.changeLead(false);
        }
    }

    private Region findRegionAndMemberValidate(Long regionId) {
        Region region = regionRepository.findWithMemberById(regionId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        memberValidate(region.getMember());
        return region;
    }

    private void memberValidate(Member member) {
        if(member == null) {
            throw new MemberException(MEMBER_NOT_FOUND);
        }
        if(!(member.getUseYn() && !member.getSuspensionYn())) {
            throw new MemberException(MEMBER_NOT_USE);
        }
    }
}
