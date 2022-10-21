package com.devwinter.supermarket.member.response;

import com.devwinter.supermarket.member.domain.Region;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class RegionListResponse {
    private int totalCount;
    private List<RegionSimpleResponse> regions = new ArrayList<>();

    @Builder
    private RegionListResponse(int totalCount, List<RegionSimpleResponse> regions) {
        this.totalCount = totalCount;
        this.regions = regions;
    }

    public static RegionListResponse of(List<Region> regions) {
        return RegionListResponse.builder()
                .totalCount(regions.size())
                .regions(regions.stream()
                        .map(r -> RegionSimpleResponse.builder()
                                .name(r.getName())
                                .authYn(authToString(r.getAuthYn()))
                                .leadYn(leadToString(r.getLeadYn()))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private static String authToString(Boolean auth) {
        return (auth) ? "인증" : "미인증";
    }

    private static String leadToString(Boolean auth) {
        return (auth) ? "대표지역" : "";
    }
}
