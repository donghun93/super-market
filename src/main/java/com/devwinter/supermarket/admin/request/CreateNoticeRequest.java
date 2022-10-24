package com.devwinter.supermarket.admin.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CreateNoticeRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    @Size(max = 5000, message = "최대 5000글자까지 입니다.")
    private String content;

    @Builder
    private CreateNoticeRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
