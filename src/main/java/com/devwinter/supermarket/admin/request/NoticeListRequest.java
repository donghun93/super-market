package com.devwinter.supermarket.admin.request;

import lombok.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class NoticeListRequest {
    private static final int MAX_SIZE = 50;

    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    public long getOffset() {
        return (long) (max(1, page) - 1) * min(size, MAX_SIZE);
    }
}
