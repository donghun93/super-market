package com.devwinter.supermarket.member.domain;

import com.devwinter.supermarket.common.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(name = "member_uni", columnNames = {"phone_number", "email"}))
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String pass;

    private Boolean suspensionYn; // 정지 여부
    private LocalDateTime lastLoginDate;
    private int mannerPoint; // 매너점수
    private Boolean useYn; // 탈퇴 여부
    private LocalDateTime resignDate; // 탈퇴 시간

    @Embedded
    private PersonalInformation personalInformation;

    @Embedded
    private Address address;

    @Builder
    private Member(String email, String pass, PersonalInformation personalInformation, Address address) {
        this.email = email;
        this.pass = pass;
        this.personalInformation = personalInformation;
        this.address = address;
        this.suspensionYn = false;
        this.mannerPoint = 0;
        this.useYn = true;
    }

    public void resign() {
        if(this.useYn) {
            this.useYn = false;
            this.resignDate = LocalDateTime.now();
        }
    }
}
