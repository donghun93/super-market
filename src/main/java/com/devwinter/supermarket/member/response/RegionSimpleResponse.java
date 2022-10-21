package com.devwinter.supermarket.member.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegionSimpleResponse {
    private String name;
    private String authYn;
    private String leadYn;

    @Builder
    private RegionSimpleResponse(String name, String authYn, String leadYn) {
        this.name = name;
        this.authYn = authYn;
        this.leadYn = leadYn;
    }
}
