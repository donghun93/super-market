package com.devwinter.supermarket.member.command.domain;

import com.devwinter.supermarket.common.domain.BaseEntity;
import com.devwinter.supermarket.member.command.exception.MemberException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.devwinter.supermarket.member.command.exception.MemberErrorCode.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MemberEmail email;

    @Embedded
    private Password pass;

    private String name;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Embedded
    private MannerPoint mannerPoint;

    private Boolean deleteYn;
    private Boolean blockYn;
    private LocalDateTime blockDate;
    private LocalDateTime deleteDate;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "member_region",
            joinColumns = {@JoinColumn(name = "member_id")})
    @OrderColumn(name = "region_idx")
    private List<Region> regions = new ArrayList<>();

    public static final int MAX_REGION_SIZE = 2;
    @Builder
    private Member(MemberEmail email, Password pass, String name, Address address, Gender gender) {
        this.email = email;
        this.pass = pass;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.mannerPoint = new MannerPoint(0);
        this.deleteYn = false;
        this.blockYn = false;
    }

    public void deleteMember() {
        if (!deleteYn) {
            deleteYn = true;
            deleteDate = LocalDateTime.now();
        }
    }

    public void createRegion(Region region) {
        if(getRegions().size() >= MAX_REGION_SIZE) {
            throw new MemberException(MEMBER_REGION_MAX);
        }

        for (Region re : getRegions()) {
            re.regionValid(region);
        }

        region.updateDate();
        getRegions().add(region);
    }

    public void regionAuthProcess(int regionIdx) {
        findRegion(regionIdx).authStatusSuccess();
    }

    public void changeLeadRegion(int regionIdx) {
        setRegionLeadStatusFalse();
        findRegion(regionIdx).changeLeadStatus(true);
    }

    private void setRegionLeadStatusFalse() {
        for (Region region : getRegions()) {
            region.changeLeadStatus(false);
        }
    }

    private Region findRegion(int regionIdx) {
        try {
            Region region = getRegions().get(regionIdx);
            if(region == null) {
                throw new MemberException(MEMBER_REGION_NOT_FOUND);
            }
            return region;
        } catch (IndexOutOfBoundsException idx) {
            throw new MemberException(MEMBER_REGION_IDX_NOT_VALID);
        }
    }

    public void deleteRegion(int regionIdx) {
        Region region = findRegion(regionIdx);
        getRegions().remove(region);
    }

    public void changeBlock() {
        if(!this.blockYn) {
            this.blockYn = true;
            this.blockDate = LocalDateTime.now();
        } else {
            this.blockYn = false;
            this.blockDate = null;
        }
    }

}
