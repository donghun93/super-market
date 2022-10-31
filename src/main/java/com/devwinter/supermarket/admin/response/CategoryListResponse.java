package com.devwinter.supermarket.admin.response;

import com.devwinter.supermarket.admin.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CategoryListResponse {
    private int totalCount;
    private List<CategoryDetailResponse> categories = new ArrayList<>();

    private CategoryListResponse(int totalCount, List<CategoryDetailResponse> categories) {
        this.totalCount = totalCount;
        this.categories = categories;
    }

    public static CategoryListResponse of(List<Category> categories) {
        return new CategoryListResponse(categories.size(),
                categories.stream().map(c -> new CategoryDetailResponse(c.getName(), c.getOrderNo())).collect(Collectors.toList()));
    }
}
