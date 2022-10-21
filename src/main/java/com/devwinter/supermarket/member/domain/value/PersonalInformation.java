package com.devwinter.supermarket.member.domain.value;

import com.devwinter.supermarket.member.domain.type.Gender;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonalInformation {

    private Gender gender;
    private Birth birth;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String name;

    @Builder
    private PersonalInformation(Gender gender, Birth birth, String phoneNumber, String name) {
        this.gender = gender;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }
}
