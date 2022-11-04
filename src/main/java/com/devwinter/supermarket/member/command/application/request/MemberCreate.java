package com.devwinter.supermarket.member.command.application.request;

import com.devwinter.supermarket.member.command.domain.*;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberCreate {
    @Email(message = "유효하지 않은 이메일 형식입니다.",
            regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;

    @NotBlank(message = "상세주소는 필수 입력 값입니다.")
    private String detail;

    @NotBlank(message = "우편번호는 필수 입력 값입니다.")
    private String zipcode;
    private Gender gender;

    public Member toEntity(String encodePassword) {
        return Member.builder()
                .email(new MemberEmail(email))
                .pass(new Password(encodePassword))
                .name(name)
                .address(new Address(address, detail, zipcode))
                .gender(gender)
                .build();
    }
}
