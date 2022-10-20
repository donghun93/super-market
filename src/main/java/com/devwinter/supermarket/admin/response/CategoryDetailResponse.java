package com.devwinter.supermarket.admin.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CategoryDetailResponse {

    private String name;
    private int orderNo;

    public CategoryDetailResponse(String name, int orderNo) {
        this.name = name;
        this.orderNo = orderNo;
    }
}
