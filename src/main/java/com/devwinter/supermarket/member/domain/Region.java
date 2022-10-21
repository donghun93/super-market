package com.devwinter.supermarket.member.domain;

import com.devwinter.supermarket.common.domain.BaseTimeEntity;
import com.devwinter.supermarket.member.domain.type.RegionRange;
import com.devwinter.supermarket.member.domain.value.RegionLocation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long id;

    private String name;
    private Boolean authYn;
    private Boolean leadYn; // 대표 지역설정 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private RegionLocation location;

    @Enumerated(EnumType.STRING)
    @Column(name = "max_range")
    private RegionRange range;

    @Builder
    private Region(String name, Member member, RegionLocation location) {
        this.name = name;
        this.member = member;
        this.location = location;
        this.authYn = false;
        this.leadYn = false;
        this.range = RegionRange.MAX;
    }

    public void changeLead(Boolean lead) {
        this.leadYn = lead;
    }

    public void authSuccess() {
        this.authYn = true;
    }

    public void changeRange(RegionRange range) {
        this.range = range;
    }
}
