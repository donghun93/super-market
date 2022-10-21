package com.devwinter.supermarket.member.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegionCreateRequest {

    private Long memberId;

    private String name;
    private Double latitude;
    private Double longitude;

    @Builder
    private RegionCreateRequest(Long memberId, String name, Double latitude, Double longitude) {
        this.memberId = memberId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
