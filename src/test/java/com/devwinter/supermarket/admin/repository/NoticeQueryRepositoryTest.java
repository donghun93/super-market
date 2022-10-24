package com.devwinter.supermarket.admin.repository;

import com.devwinter.supermarket.admin.domain.Notice;
import com.devwinter.supermarket.admin.exception.NoticeErrorCode;
import com.devwinter.supermarket.admin.exception.NoticeException;
import com.devwinter.supermarket.admin.response.NoticeDetailResponse;
import com.devwinter.supermarket.admin.response.NoticeSimpleResponse;
import com.devwinter.supermarket.member.domain.Member;
import com.devwinter.supermarket.member.domain.value.Birth;
import com.devwinter.supermarket.member.domain.value.PersonalInformation;
import com.devwinter.supermarket.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test"})
@Transactional
class NoticeQueryRepositoryTest {

    @Autowired
    private NoticeQueryRepository noticeQueryRepository;
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("공지사항 리스트 조회 테스트")
    void getNoticeAllTest() {
        // given
        for (int i = 1; i <= 100; i++) {

            Member member = Member.builder()
                    .email("test@gamil.com")
                    .pass("12312" + i)
                    .build();

            memberRepository.save(member);

            noticeRepository.save(Notice.builder()
                    .title("제목입니다. " + i)
                    .content("본문입니다. " + i)
                    .member(member)
                    .build());
        }

        int page = 2;
        int size = 5;

        PageRequest pageable = PageRequest.of(page, size);

        // when
        Page<NoticeSimpleResponse> response = noticeQueryRepository.getNoticeAll(pageable);

        // then
        assertThat(response.getTotalElements()).isEqualTo(100L);
        assertThat(response.getNumberOfElements()).isEqualTo(size);
    }


    @Test
    @DisplayName("공지사항 조회 시 회원 fetch join 테스트")
    void getNoticeWithMemberTest() {
        // given
        Member member = Member.builder()
                .email("test@gmail.com")
                .pass("hello")
                .personalInformation(PersonalInformation.builder()
                        .name("테스터")
                        .birth(Birth.builder()
                                .year("2022")
                                .month("10")
                                .day("24")
                                .build())
                        .build())
                .build();
        memberRepository.save(member);

        Notice notice = Notice.builder()
                .member(member)
                .title("테스트 공지사항입니다.")
                .content("테스트 내용입니다. ")
                .build();
        noticeRepository.save(notice);

        em.flush();
        em.clear();

        // when
        NoticeDetailResponse noticeDetailResponse = noticeQueryRepository.getNoticeDetail(notice.getId())
                .orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));

        // then
        assertThat(noticeDetailResponse.getId()).isEqualTo(notice.getId());
        assertThat(noticeDetailResponse.getTitle()).isEqualTo(notice.getTitle());
        assertThat(noticeDetailResponse.getContent()).isEqualTo(notice.getContent());
        assertThat(noticeDetailResponse.getName()).isEqualTo("테스터");
    }
}