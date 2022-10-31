package com.devwinter.supermarket.admin.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class NoticeListResponse {
    private Long totalCount;
    private List<NoticeSimpleResponse> notices = new ArrayList<>();

    @Builder
    private NoticeListResponse(Long totalCount, List<NoticeSimpleResponse> notices) {
        this.totalCount = totalCount;
        this.notices = notices;
    }
}
