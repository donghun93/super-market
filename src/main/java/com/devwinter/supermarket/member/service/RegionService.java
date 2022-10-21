package com.devwinter.supermarket.member.service;

import com.devwinter.supermarket.member.domain.type.RegionRange;
import com.devwinter.supermarket.member.request.RegionCreateRequest;
import com.devwinter.supermarket.member.response.RegionListResponse;

public interface RegionService {
    void registerRegion(RegionCreateRequest request);
    void setLeadRegion(Long regionId);
    void authRegion(Long regionId);
    void setRegionRange(Long regionId, RegionRange range);
    void deleteRegion(Long regionId);
    RegionListResponse getRegionList(Long memberId);
}
