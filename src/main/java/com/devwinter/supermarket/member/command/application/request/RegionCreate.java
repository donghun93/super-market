package com.devwinter.supermarket.member.command.application.request;


import com.devwinter.supermarket.member.command.domain.Location;
import com.devwinter.supermarket.member.command.domain.Region;
import com.devwinter.supermarket.member.command.domain.RegionRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionCreate {
    private String name;
    private Double latitude;
    private Double longitude;

    public Region toValue() {
        return new Region(name, new Location(latitude, longitude));
    }
}
