package com.devwinter.supermarket.admin.service;

import com.devwinter.supermarket.admin.request.CreateNoticeRequest;
import com.devwinter.supermarket.admin.response.NoticeDetailResponse;
import com.devwinter.supermarket.admin.response.NoticeSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeService {

    void createNotice(Long memberId, CreateNoticeRequest request);
    void updateNotice(Long memberId, Long noticeId, CreateNoticeRequest request);
    void deleteNotice(Long memberId, Long noticeId);
    Page<NoticeSimpleResponse> getNoticeList(Pageable pageable);
    NoticeDetailResponse getNoticeDetail(Long noticeId);
    void changedShowNotice(Long noticeId, Boolean visible);
}
