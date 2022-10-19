package com.devwinter.supermarket.admin.request;

import com.devwinter.supermarket.admin.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CreateCategoryRequest {

    @Size(min = 1, max = 15, message = "카테고리명은 최소 1~15글자 사이로 입력해주세요.")
    private String name;

    @Min(value = 1, message = "정렬 순서는 최소 1이상입니다.")
    private int orderNo;

    @Builder
    private CreateCategoryRequest(Long parentId, String name, int orderNo) {
        this.name = name;
        this.orderNo = orderNo;
    }

    public Category toEntity() {
        return Category.builder()
                .name(this.name)
                .orderNo(this.orderNo)
                .build();
    }
}
