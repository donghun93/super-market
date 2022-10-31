package com.devwinter.supermarket.member.command.application.request;

import com.devwinter.supermarket.member.command.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberCreate {

    private String email;
    private String password;
    private String name;
    private Address address;
    private Gender gender;

    public Member toEntity(String encodePassword) {
        return Member.builder()
                .email(new Email(email))
                .pass(new Password(encodePassword))
                .name(name)
                .address(new Address(address.getAddress(), address.getDetail(), address.getZipcode()))
                .gender(gender)
                .build();
    }
}
