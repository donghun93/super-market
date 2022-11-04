package com.devwinter.supermarket.admin.member.command.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRoleStatus {
    private Long id;
    private String name;
    private String desc;
}
