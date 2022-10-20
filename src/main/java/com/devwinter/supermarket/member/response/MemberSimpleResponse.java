package com.devwinter.supermarket.member.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSimpleResponse {
    private Long id;
    private String suspensionYn;
    private String lastLoginDate;
    private String name;
    private String joinDate;
    private String useYn;
    private String resignDate;

    @Builder
    private MemberSimpleResponse(Long id, String suspensionYn, String lastLoginDate, String name, String joinDate, String useYn, String resignDate) {
        this.id = id;
        this.suspensionYn = suspensionYn;
        this.lastLoginDate = lastLoginDate;
        this.name = name;
        this.joinDate = joinDate;
        this.useYn = useYn;
        this.resignDate = resignDate;
    }
}
