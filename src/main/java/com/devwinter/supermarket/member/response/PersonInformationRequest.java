package com.devwinter.supermarket.member.response;

import com.devwinter.supermarket.member.domain.type.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PersonInformationRequest {
    private String name;
    private String phoneNumber;
    private String year;
    private String month;
    private String day;
    private Gender gender;

    @Builder
    private PersonInformationRequest(String name, String phoneNumber, String year, String month, String day, Gender gender) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.year = year;
        this.month = month;
        this.day = day;
        this.gender = gender;
    }
}
