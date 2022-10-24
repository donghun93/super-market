package com.devwinter.supermarket.admin.response;

import com.devwinter.supermarket.common.utils.StringConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDetailResponse {

    private Long id;
    private String title;
    private String content;
    private String name;
    private String createDate;

    public NoticeDetailResponse(Long id, String title, String content, String name, LocalDateTime createDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.name = name;
        this.createDate = StringConverter.localDateTimeToString(createDate);
    }
}
