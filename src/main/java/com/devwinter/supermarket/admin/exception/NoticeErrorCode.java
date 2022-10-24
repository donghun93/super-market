package com.devwinter.supermarket.admin.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeErrorCode {

    NOTICE_CREATE_NOT_AUTHORITY ("권한이 없어 공지사항을 만들 수 없습니다."),
    NOTICE_UPDATE_NOT_AUTHORITY ("권한이 없어 공지사항을 수정할 수 없습니다."),
    NOTICE_DELETE_NOT_AUTHORITY ("권한이 없어 공지사항을 삭제할 수 없습니다."),
    NOTICE_NOT_FOUND("공지사항이 존재하지 않습니다.")
    ;

    private final String description;
}
