package com.devwinter.supermarket.member.response;

import com.devwinter.supermarket.member.domain.Address;
import com.devwinter.supermarket.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberDetailResponse {

    private Long id;
    private String email;
    private PersonInformationRequest info;
    private Address address;

    @Builder
    private MemberDetailResponse(Long id, String email, PersonInformationRequest info, Address address) {
        this.id = id;
        this.email = email;
        this.info = info;
        this.address = address;
    }

    public static MemberDetailResponse of(Member member) {
        return MemberDetailResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .info(PersonInformationRequest.builder()
                        .name(member.getPersonalInformation().getName())
                        .phoneNumber(member.getPersonalInformation().getPhoneNumber())
                        .year(member.getPersonalInformation().getBirth().getYear())
                        .month(member.getPersonalInformation().getBirth().getMonth())
                        .day(member.getPersonalInformation().getBirth().getDay())
                        .gender(member.getPersonalInformation().getGender())
                        .build())
                .address(Address.builder()
                        .city(member.getAddress().getCity())
                        .address(member.getAddress().getAddress())
                        .zipcode(member.getAddress().getZipcode())
                        .build())
                .build();
    }

}
