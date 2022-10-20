package com.devwinter.supermarket.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String city;
    private String address;
    private String zipcode;

    @Builder
    private Address(String city, String address, String zipcode) {
        this.city = city;
        this.address = address;
        this.zipcode = zipcode;
    }
}
