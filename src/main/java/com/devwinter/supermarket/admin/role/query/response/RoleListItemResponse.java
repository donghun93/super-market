package com.devwinter.supermarket.admin.role.query.response;

import com.devwinter.supermarket.common.utils.StringConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NegativeOrZero;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RoleListItemResponse {

    private Long id;
    private String name;
    private String desc;

    public RoleListItemResponse(Long id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }
}
