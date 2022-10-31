package com.devwinter.supermarket.admin.service.impl;

import com.devwinter.supermarket.admin.domain.Notice;
import com.devwinter.supermarket.admin.exception.NoticeErrorCode;
import com.devwinter.supermarket.admin.exception.NoticeException;
import com.devwinter.supermarket.admin.repository.NoticeQueryRepository;
import com.devwinter.supermarket.admin.repository.NoticeRepository;
import com.devwinter.supermarket.admin.request.CreateNoticeRequest;
import com.devwinter.supermarket.admin.response.NoticeDetailResponse;
import com.devwinter.supermarket.admin.response.NoticeSimpleResponse;
import com.devwinter.supermarket.admin.service.NoticeService;
import com.devwinter.supermarket.member.domain.Member;
import com.devwinter.supermarket.member.exception.MemberException;
import com.devwinter.supermarket.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.devwinter.supermarket.admin.exception.NoticeErrorCode.*;
import static com.devwinter.supermarket.member.domain.type.MemberRole.ADMIN;
import static com.devwinter.supermarket.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeQueryRepository noticeQueryRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void createNotice(Long memberId, CreateNoticeRequest request) {
        Member findMember = findMember(memberId);
        validateAuthorityMember(findMember, NOTICE_CREATE_NOT_AUTHORITY);

        Notice notice = Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(findMember)
                .build();

        noticeRepository.save(notice);
    }

    @Override
    @Transactional
    public void updateNotice(Long memberId, Long noticeId, CreateNoticeRequest request) {
        Member findMember = findMember(memberId);
        validateAuthorityMember(findMember, NOTICE_UPDATE_NOT_AUTHORITY);

        Notice findNotice = findNotice(noticeId);
        findNotice.updateNotice(request.getTitle(), request.getContent(), findMember);
    }

    @Override
    @Transactional
    public void deleteNotice(Long memberId, Long noticeId) {
        Member findMember = findMember(memberId);
        validateAuthorityMember(findMember, NOTICE_DELETE_NOT_AUTHORITY);

        Notice findNotice = findNotice(noticeId);
        findNotice.deleteNotice();
    }


    @Override
    public Page<NoticeSimpleResponse> getNoticeList(Pageable pageable) {
        return noticeQueryRepository.getNoticeAll(pageable);
    }

    @Override
    public NoticeDetailResponse getNoticeDetail(Long noticeId) {
        return noticeQueryRepository.getNoticeDetail(noticeId)
                .orElseThrow(() -> new NoticeException(NOTICE_NOT_FOUND));
    }

    @Override
    public void changedShowNotice(Long noticeId, Boolean showYn) {
        Notice findNotice = findNotice(noticeId);
        findNotice.changeShow(showYn);
    }

    private void validateAuthorityMember(Member findMember, NoticeErrorCode errorCode) {
        if(findMember.getMemberRole() != ADMIN) {
            throw new NoticeException(errorCode);
        }
    }

    private Notice findNotice(Long noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeException(NOTICE_NOT_FOUND));
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }
}
