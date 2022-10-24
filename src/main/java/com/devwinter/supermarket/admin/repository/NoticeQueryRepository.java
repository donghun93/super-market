package com.devwinter.supermarket.admin.repository;

import com.devwinter.supermarket.admin.response.NoticeDetailResponse;
import com.devwinter.supermarket.admin.response.NoticeSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NoticeQueryRepository {
    Page<NoticeSimpleResponse> getNoticeAll(Pageable pageable);

    Optional<NoticeDetailResponse> getNoticeDetail(Long noticeId);
}
