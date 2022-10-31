package com.devwinter.supermarket.member.command.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String address;
    private String detail;
    private String zipcode;

    public Address(String address, String detail, String zipcode) {
        this.address = address;
        this.detail = detail;
        this.zipcode = zipcode;
    }
}
