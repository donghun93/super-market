package com.devwinter.supermarket.admin.service;

import com.devwinter.supermarket.admin.domain.Notice;
import com.devwinter.supermarket.admin.exception.NoticeException;
import com.devwinter.supermarket.admin.repository.NoticeQueryRepository;
import com.devwinter.supermarket.admin.repository.NoticeRepository;
import com.devwinter.supermarket.admin.request.CreateNoticeRequest;
import com.devwinter.supermarket.admin.response.NoticeDetailResponse;
import com.devwinter.supermarket.admin.response.NoticeListResponse;
import com.devwinter.supermarket.admin.service.impl.NoticeServiceImpl;
import com.devwinter.supermarket.member.domain.Member;
import com.devwinter.supermarket.member.domain.type.MemberRole;
import com.devwinter.supermarket.member.exception.MemberException;
import com.devwinter.supermarket.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.devwinter.supermarket.admin.exception.NoticeErrorCode.*;
import static com.devwinter.supermarket.member.domain.type.MemberRole.ADMIN;
import static com.devwinter.supermarket.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private NoticeQueryRepository noticeQueryRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private NoticeServiceImpl noticeService;

    @Test
    @DisplayName("공지사항 등록 시 회원이 존재하지 않을 경우 테스트")
    void createNoticeNotFoundMemberTest() {
        // given
        given(memberRepository.findById(anyLong()))
                .willThrow(new MemberException(MEMBER_NOT_FOUND));

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> noticeService.createNotice(1L, any()));

        // then
        verify(memberRepository, times(1)).findById(anyLong());
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.toString());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("공지사항 등록 테스트")
    void noticeCreateTest() {
        // given
        CreateNoticeRequest createNoticeRequest = CreateNoticeRequest.builder()
                .title("공지사항 테스트입니다.")
                .content("공지사항 테스트 본문입니다.")
                .build();

        Member member = Member.builder()
                .email("admin@gmail.com")
                .build();
        member.changeMemberRole(ADMIN);

        Member mockMember = spy(member);

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(mockMember));

        ArgumentCaptor<Notice> captor = ArgumentCaptor.forClass(Notice.class);

        // when
        noticeService.createNotice(anyLong(), createNoticeRequest);
        verify(noticeRepository).save(captor.capture());

        // then
        verify(memberRepository, times(1)).findById(anyLong());
        verify(mockMember, times(1)).getMemberRole();

        assertThat(captor.getValue().getTitle()).isEqualTo("공지사항 테스트입니다.");
        assertThat(captor.getValue().getContent()).isEqualTo("공지사항 테스트 본문입니다.");
    }

    @Test
    @DisplayName("공지사항 등록 시 권한 없을 경우")
    void createNoticeNotAuthorityTest() {
        // given
        Member member = Member.builder()
                .email("admin@gmail.com")
                .build();
        Member mockMember = spy(member);

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(mockMember));

        // when
        NoticeException noticeException = assertThrows(NoticeException.class,
                () -> noticeService.createNotice(1L, any()));

        // then
        verify(mockMember, times(1)).getMemberRole();
        assertThat(noticeException.getErrorCode()).isEqualTo(NOTICE_CREATE_NOT_AUTHORITY.toString());
        assertThat(noticeException.getErrorMessage()).isEqualTo(NOTICE_CREATE_NOT_AUTHORITY.getDescription());
    }

    @Test
    @DisplayName("공지사항 업데이트 시 회원이 존재하지 않을 경우 테스트")
    void updateNoticeNotFoundMemberTest() {
        // given
        given(memberRepository.findById(anyLong()))
                .willThrow(new MemberException(MEMBER_NOT_FOUND));

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> noticeService.updateNotice(1L, 2L, any()));

        // then
        verify(memberRepository, times(1)).findById(anyLong());
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.toString());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("공지사항 업데이트시 공지사항 못찾을 경우 테스트")
    void updateNoticeNotFoundTest() {
        // given
        Member member = Member.builder()
                .email("admin@gmail.com")
                .build();
        member.changeMemberRole(ADMIN);

        Member mockMember = spy(member);

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(mockMember));

        given(noticeRepository.findById(anyLong()))
                .willThrow(new NoticeException(NOTICE_NOT_FOUND));

        // when
        NoticeException noticeException = assertThrows(NoticeException.class,
                () -> noticeService.updateNotice(1L, 1L, any()));

        // then
        verify(memberRepository, times(1)).findById(anyLong());
        verify(mockMember, times(1)).getMemberRole();
        verify(noticeRepository, times(1)).findById(anyLong());
        assertThat(noticeException.getErrorCode()).isEqualTo(NOTICE_NOT_FOUND.toString());
        assertThat(noticeException.getErrorMessage()).isEqualTo(NOTICE_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("공지사항 업데이트 시 권한 없을 경우 실패 테스트")
    void updateNoticeNotAuthorityTest() {
        // given
        Member member = Member.builder()
                .email("admin@gmail.com")
                .build();
        Member mockMember = spy(member);

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(mockMember));

        // when
        NoticeException noticeException = assertThrows(NoticeException.class,
                () -> noticeService.updateNotice(1L, 1L, any()));

        // then
        verify(mockMember, times(1)).getMemberRole();
        assertThat(noticeException.getErrorCode()).isEqualTo(NOTICE_UPDATE_NOT_AUTHORITY.toString());
        assertThat(noticeException.getErrorMessage()).isEqualTo(NOTICE_UPDATE_NOT_AUTHORITY.getDescription());
    }

    @Test
    @DisplayName("공지사항 업데이트 테스트")
    void updateNoticeTest() {
        // given
        Member member = Member.builder()
                .email("admin@gmail.com")
                .build();
        member.changeMemberRole(ADMIN);
        Member mockMember = spy(member);

        Notice notice = Notice.builder()
                .title("테스트 제목")
                .content("테스트 본문")
                .build();
        Notice mockNotice = spy(notice);

        CreateNoticeRequest request = CreateNoticeRequest.builder()
                .title("변경 제목")
                .content("변경 본문")
                .build();


        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(mockMember));

        given(noticeRepository.findById(anyLong()))
                .willReturn(Optional.of(mockNotice));

        // when
        noticeService.updateNotice(1L, 1L, request);

        // then
        verify(memberRepository, times(1)).findById(anyLong());
        verify(mockMember, times(1)).getMemberRole();
        verify(noticeRepository, times(1)).findById(anyLong());
        verify(mockNotice, times(1)).updateNotice(anyString(), anyString(), any());
        assertThat(mockNotice.getTitle()).isEqualTo("변경 제목");
        assertThat(mockNotice.getContent()).isEqualTo("변경 본문");
    }

    @Test
    @DisplayName("공지사항 삭제 시 공지사항 없을 경우 테스트")
    void deleteNoticeNotFoundTest() {
        // given
        Member member = Member.builder()
                .email("admin@gmail.com")
                .build();
        member.changeMemberRole(ADMIN);

        Member mockMember = spy(member);

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(mockMember));

        given(noticeRepository.findById(anyLong()))
                .willThrow(new NoticeException(NOTICE_NOT_FOUND));

        // when
        NoticeException noticeException = assertThrows(NoticeException.class,
                () -> noticeService.deleteNotice(1L, 1L));

        // then
        verify(memberRepository, times(1)).findById(anyLong());
        verify(mockMember, times(1)).getMemberRole();
        verify(noticeRepository, times(1)).findById(anyLong());
        assertThat(noticeException.getErrorCode()).isEqualTo(NOTICE_NOT_FOUND.toString());
        assertThat(noticeException.getErrorMessage()).isEqualTo(NOTICE_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("공지사항 삭제 시 권한 없을 경우 실패 테스트")
    void deleteNoticeNotAuthorityTest() {
        // given
        Member member = Member.builder()
                .email("admin@gmail.com")
                .build();
        Member mockMember = spy(member);

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(mockMember));

        // when
        NoticeException noticeException = assertThrows(NoticeException.class,
                () -> noticeService.deleteNotice(1L, 1L));

        // then
        verify(mockMember, times(1)).getMemberRole();
        assertThat(noticeException.getErrorCode()).isEqualTo(NOTICE_DELETE_NOT_AUTHORITY.toString());
        assertThat(noticeException.getErrorMessage()).isEqualTo(NOTICE_DELETE_NOT_AUTHORITY.getDescription());
    }

    @Test
    @DisplayName("공지사항 삭제 성공 테스트")
    void deleteNoticeTest() {
        // given
        Member member = Member.builder()
                .email("admin@gmail.com")
                .build();
        member.changeMemberRole(ADMIN);
        Member mockMember = spy(member);

        Notice notice = Notice.builder()
                .title("테스트 제목")
                .content("테스트 본문")
                .build();
        Notice mockNotice = spy(notice);

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(mockMember));

        given(noticeRepository.findById(anyLong()))
                .willReturn(Optional.of(mockNotice));

        // when
        noticeService.deleteNotice(1L, 1L);

        // then
        verify(memberRepository, times(1)).findById(anyLong());
        verify(mockMember, times(1)).getMemberRole();
        verify(noticeRepository, times(1)).findById(anyLong());
        verify(mockNotice, times(1)).deleteNotice();
        assertThat(mockNotice.getDeleteYn()).isTrue();
    }

    @Test
    @DisplayName("공지사항 상세조회 없을경우 테스트")
    void getNoticeDetailNotFoundTest() {
        // given
        given(noticeQueryRepository.getNoticeDetail(anyLong()))
                .willThrow(new NoticeException(NOTICE_NOT_FOUND));
    
        // when
        NoticeException noticeException = assertThrows(NoticeException.class, () -> noticeService.getNoticeDetail(anyLong()));

        // then
        verify(noticeQueryRepository, times(1)).getNoticeDetail(anyLong());
        assertThat(noticeException.getErrorCode()).isEqualTo(NOTICE_NOT_FOUND.toString());
        assertThat(noticeException.getErrorMessage()).isEqualTo(NOTICE_NOT_FOUND.getDescription());
    }
    
    @Test
    @DisplayName("공지사항 상세조회 테스트")
    void getNoticeDetailTest() {
        // given
        NoticeDetailResponse detail = NoticeDetailResponse
                .builder()
                .id(1L)
                .title("테스트 제목")
                .content("테스트 본문")
                .name("테스터")
                .createDate("2022-10-24 13:22:35")
                .build();

        given(noticeQueryRepository.getNoticeDetail(anyLong()))
                .willReturn(Optional.of(detail));

        // when
        NoticeDetailResponse response = noticeService.getNoticeDetail(anyLong());


        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("테스트 제목");
        assertThat(response.getContent()).isEqualTo("테스트 본문");
        assertThat(response.getName()).isEqualTo("테스터");
        assertThat(response.getCreateDate()).isEqualTo("2022-10-24 13:22:35");
    }

    @Test
    @DisplayName("공지사항 상태변경시 공지사항 없을경우 테스트")
    void changedShowNoticeNotFoundTest() {
        // given
        given(noticeRepository.findById(anyLong()))
                .willThrow(new NoticeException(NOTICE_NOT_FOUND));

        // when
        NoticeException noticeException = assertThrows(NoticeException.class, () -> noticeService.changedShowNotice(1L, anyBoolean()));

        // then
        verify(noticeRepository, times(1)).findById(anyLong());
        assertThat(noticeException.getErrorCode()).isEqualTo(NOTICE_NOT_FOUND.toString());
        assertThat(noticeException.getErrorMessage()).isEqualTo(NOTICE_NOT_FOUND.getDescription());
    }


    @ParameterizedTest
    @ValueSource(strings = {"true", "false"})
    @DisplayName("공지사항 상태변경 테스트")
    void changedShowNoticeTest(Boolean showYn) {
        // given
        Notice notice = Notice.builder()
                .title("테스트 제목")
                .content("테스트 본문")
                .build();

        Notice mockNotice = spy(notice);
        given(noticeRepository.findById(anyLong()))
                .willReturn(Optional.of(mockNotice));

        // when
        noticeService.changedShowNotice(anyLong(), showYn);


        // then
        verify(mockNotice, times(1)).changeShow(showYn);
        assertThat(mockNotice.getShowYn()).isEqualTo(showYn);
    }


    
}