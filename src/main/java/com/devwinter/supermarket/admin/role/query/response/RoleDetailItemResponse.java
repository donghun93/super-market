package com.devwinter.supermarket.admin.role.query.response;

import com.devwinter.supermarket.common.utils.StringConverter;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RoleDetailItemResponse {
    private Long id;
    private String name;
    private String desc;
    private String createdBy;
    private String createDate;

    public RoleDetailItemResponse(Long id, String name, String desc, String createdBy, LocalDateTime createDate) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.createdBy = createdBy;
        this.createDate = StringConverter.localDateTimeToString(createDate);
    }
}
