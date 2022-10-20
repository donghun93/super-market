package com.devwinter.supermarket.member.request;

import com.devwinter.supermarket.member.domain.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@NoArgsConstructor
public class MemberCreateRequest {

    private String email;
    private String pass;
    private String name;
    private String phoneNumber;
    private String year;
    private String month;
    private String day;
    private Gender gender;
    private String city;
    private String address;
    private String zipcode;

    @Builder
    private MemberCreateRequest(String email, String pass, String name, String phoneNumber, String year, String month, String day, Gender gender, String city, String address, String zipcode) {
        this.email = email;
        this.pass = pass;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.year = year;
        this.month = month;
        this.day = day;
        this.gender = gender;
        this.city = city;
        this.address = address;
        this.zipcode = zipcode;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .pass(pass)
                .personalInformation(PersonalInformation.builder()
                        .name(name)
                        .phoneNumber(phoneNumber)
                        .birth(Birth.builder()
                                .year(year)
                                .month(month)
                                .day(day)
                                .build())
                        .gender(gender)
                        .build())
                .address(Address.builder()
                        .city(city)
                        .address(address)
                        .zipcode(zipcode)
                        .build())
                .build();
    }
}
