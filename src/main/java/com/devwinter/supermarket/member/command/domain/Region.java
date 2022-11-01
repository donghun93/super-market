package com.devwinter.supermarket.member.command.domain;

import com.devwinter.supermarket.member.command.exception.MemberException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.devwinter.supermarket.member.command.exception.MemberErrorCode.MEMBER_ALREADY_REGION_NAME;
import static com.devwinter.supermarket.member.command.exception.MemberErrorCode.MEMBER_ALREADY_REGION_REGISTER;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region {

    private String name;
    private boolean authStatus; // 동네인증
    private boolean leadStatus; // 대표동네

    @Embedded
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(name = "max_range")
    private RegionRange range;

    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public Region(String name, Location location) {
        this.name = name;
        this.location = location;
        this.range = RegionRange.NORMAL;
        this.authStatus = false;
        this.leadStatus = false;
    }

    public void authStatusSuccess() {
        updateDate();
        this.authStatus = true;
    }

    public void changeLeadStatus(boolean leadStatus) {
        updateDate();
        this.leadStatus = leadStatus;
    }

    public void regionValid(Region region) {
        if (this.name.equals(region.getName())) {
            throw new MemberException(MEMBER_ALREADY_REGION_NAME);
        }
        if (Objects.equals(this.location.getLatitude(), region.getLocation().getLatitude()) &&
                Objects.equals(this.location.getLongitude(), region.getLocation().getLongitude())) {
            throw new MemberException(MEMBER_ALREADY_REGION_REGISTER);
        }
    }

    public void updateDate() {
        LocalDateTime localDateTime = LocalDateTime.now();

        if(createdDate == null) {
            createdDate = localDateTime;
        }
        modifiedDate = localDateTime;
    }
}
