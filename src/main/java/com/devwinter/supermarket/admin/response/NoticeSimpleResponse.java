package com.devwinter.supermarket.admin.response;


import com.devwinter.supermarket.common.utils.StringConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeSimpleResponse {
 
    private Long orderNo; // No
    private String title; // 제목
    private String createBy; // 등록자
    private String status; //상태
    private String deleteYn; //삭제여부
    private String createDate; // 최초생성일
    private String updateDate; // 최종수정일

    public NoticeSimpleResponse(Long orderNo, String title, String createBy, Boolean showYn, Boolean deleteYn, LocalDateTime createDate,
                                LocalDateTime updateDate) {
        this.orderNo = orderNo;
        this.title = title;
        this.createBy = createBy;
        this.status = statusToString(showYn);
        this.deleteYn = deleteYnToString(deleteYn);
        this.createDate = StringConverter.localDateTimeToString(createDate);
        this.updateDate = StringConverter.localDateTimeToString(updateDate);
    }

    private String statusToString(Boolean showYn) {
        return (showYn) ? "OPEN" : "CLOSE";
    }

    private String deleteYnToString(Boolean deleteYn) {
        return (deleteYn) ? "Y" : "N";
    }

}
