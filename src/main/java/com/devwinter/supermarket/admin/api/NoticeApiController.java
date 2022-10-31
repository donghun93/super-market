package com.devwinter.supermarket.admin.api;

import com.devwinter.supermarket.admin.request.CreateNoticeRequest;
import com.devwinter.supermarket.admin.request.NoticeListRequest;
import com.devwinter.supermarket.admin.response.NoticeDetailResponse;
import com.devwinter.supermarket.admin.response.NoticeListResponse;
import com.devwinter.supermarket.admin.response.NoticeSimpleResponse;
import com.devwinter.supermarket.admin.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
public class NoticeApiController {

    private final NoticeService noticeService;

    @GetMapping
    public Page<NoticeSimpleResponse> getNoticeList(Pageable pageable) {
        return noticeService.getNoticeList(pageable);
    }

    @GetMapping("/{noticeId}")
    public NoticeDetailResponse getNoticeDetail(@PathVariable Long noticeId) {
        return noticeService.getNoticeDetail(noticeId);
    }

    @PostMapping
    public void createNotice(@Valid @RequestBody CreateNoticeRequest request) {
        noticeService.createNotice(2L, request);
    }
}
