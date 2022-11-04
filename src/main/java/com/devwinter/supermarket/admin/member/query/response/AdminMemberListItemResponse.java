package com.devwinter.supermarket.admin.member.query.response;

import com.devwinter.supermarket.common.utils.StringConverter;
import com.devwinter.supermarket.member.command.domain.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminMemberListItemResponse {
    private Long id;
    private String email;
    private String name;
    private Gender gender;
    private String deleteYn;
    private String blockYn;
    private int mannerPoint;
    private String createdDate;
    private String roleName;

    public AdminMemberListItemResponse(Long id, String email, String name, Gender gender, Boolean deleteYn, Boolean blockYn, int mannerPoint, LocalDateTime createdDate, String roleName, String roleDesc) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.deleteYn = (deleteYn) ? "삭제" : "사용";
        this.blockYn = (blockYn) ? "잠금" : "잠금해제";
        this.mannerPoint = mannerPoint;
        this.createdDate = StringConverter.localDateTimeToString(createdDate);
        this.roleName = roleDesc + "(" +  roleName + ")";
    }
}
