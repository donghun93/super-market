package com.devwinter.supermarket.member.command.domain;

import com.devwinter.supermarket.common.domain.BaseEntity;
import com.devwinter.supermarket.role.command.domain.RoleId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Password pass;

    private String name;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Embedded
    private MannerPoint mannerPoint;

    private boolean deleteYn;
    private LocalDateTime deleteDate;
    
    @Builder
    private Member(Email email, Password pass, String name, Address address, Gender gender) {
        this.email = email;
        this.pass = pass;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.mannerPoint = new MannerPoint(0);
        this.deleteYn = false;
    }
}
