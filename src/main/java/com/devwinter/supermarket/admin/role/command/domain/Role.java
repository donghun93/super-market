package com.devwinter.supermarket.admin.role.command.domain;

import com.devwinter.supermarket.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_name")
    private String name;

    @Column(name = "role_desc")
    private String desc;

    @Builder
    private Role(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public void changeRole(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
}
