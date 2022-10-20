package com.devwinter.supermarket.member.service;

import com.devwinter.supermarket.member.domain.*;
import com.devwinter.supermarket.member.exception.MemberException;
import com.devwinter.supermarket.member.repository.MemberRepository;
import com.devwinter.supermarket.member.request.MemberCreateRequest;
import com.devwinter.supermarket.member.response.MemberDetailResponse;
import com.devwinter.supermarket.member.response.MemberListResponse;
import com.devwinter.supermarket.member.response.PersonInformationRequest;
import com.devwinter.supermarket.member.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.devwinter.supermarket.member.exception.MemberErrorCode.MEMBER_ALREADY_EXIST;
import static com.devwinter.supermarket.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test"})
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("회원 저장 테스트")
    void createMemberTest() {
        // given
        MemberCreateRequest memberCreateRequest = MemberCreateRequest.builder()
                .email("test@gmail.com")
                .pass("test1234")
                .name("테스트")
                .phoneNumber("010-1111-2222")
                .year("1993")
                .month("05")
                .day("08")
                .gender(Gender.MAN)
                .city("서울시")
                .address("마포구")
                .zipcode("12345")
                .build();

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

        // when
        memberService.createMember(memberCreateRequest);

        // then
        verify(memberRepository, times(1))
                .save(captor.capture());

        assertThat(memberCreateRequest.getEmail())
                .isEqualTo(captor.getValue().getEmail());
        assertThat(memberCreateRequest.getPass())
                .isEqualTo(captor.getValue().getPass());
        assertThat(memberCreateRequest.getName())
                .isEqualTo(captor.getValue().getPersonalInformation().getName());
        assertThat(memberCreateRequest.getPhoneNumber())
                .isEqualTo(captor.getValue().getPersonalInformation().getPhoneNumber());
        assertThat(memberCreateRequest.getYear())
                .isEqualTo(captor.getValue().getPersonalInformation().getBirth().getYear());
        assertThat(memberCreateRequest.getMonth())
                .isEqualTo(captor.getValue().getPersonalInformation().getBirth().getMonth());
        assertThat(memberCreateRequest.getDay())
                .isEqualTo(captor.getValue().getPersonalInformation().getBirth().getDay());
        assertThat(memberCreateRequest.getGender())
                .isEqualTo(captor.getValue().getPersonalInformation().getGender());
        assertThat(memberCreateRequest.getCity())
                .isEqualTo(captor.getValue().getAddress().getCity());
        assertThat(memberCreateRequest.getAddress())
                .isEqualTo(captor.getValue().getAddress().getAddress());
        assertThat(memberCreateRequest.getZipcode())
                .isEqualTo(captor.getValue().getAddress().getZipcode());
    }

    @Test
    @DisplayName("회원 중복 실패 테스트")
    void createMemberDuplicateTest() {
        // given
        MemberCreateRequest memberCreateRequest = MemberCreateRequest.builder()
                .email("test@gmail.com")
                .pass("test1234")
                .name("테스트")
                .phoneNumber("010-1111-2222")
                .year("1993")
                .month("05")
                .day("08")
                .gender(Gender.MAN)
                .city("서울시")
                .address("마포구")
                .zipcode("12345")
                .build();

        given(memberRepository.findDuplicateMember(anyString(), anyString()))
                .willThrow(new MemberException(MEMBER_ALREADY_EXIST));

        // when
        MemberException memberException = assertThrows(MemberException.class, () -> memberService.createMember(memberCreateRequest));

        // then
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_ALREADY_EXIST.toString());
    }

    @Test
    @DisplayName("회원 이메일로 조회 테스트")
    void findMemberByEmail() {
        Member member = Member.builder()
                .email("test")
                .pass("test")
                .personalInformation(PersonalInformation.builder()
                        .gender(Gender.MAN)
                        .birth(Birth.builder()
                                .year("2022")
                                .month("10")
                                .day("20")
                                .build())
                        .phoneNumber("01011112222")
                        .name("test")
                        .build())
                .address(Address.builder()
                        .city("서울")
                        .address("마포")
                        .zipcode("10121")
                        .build())
                .build();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));

        // when
        MemberDetailResponse response = memberService.getMember("test");

        // then
        assertThat(response.getEmail()).isEqualTo(member.getEmail());
        assertThat(response.getInfo().getYear()).isEqualTo(member.getPersonalInformation().getBirth().getYear());
        assertThat(response.getInfo().getMonth()).isEqualTo(member.getPersonalInformation().getBirth().getMonth());
        assertThat(response.getInfo().getDay()).isEqualTo(member.getPersonalInformation().getBirth().getDay());
        assertThat(response.getInfo().getName()).isEqualTo(member.getPersonalInformation().getName());
        assertThat(response.getInfo().getPhoneNumber()).isEqualTo(member.getPersonalInformation().getPhoneNumber());
        assertThat(response.getInfo().getGender()).isEqualTo(member.getPersonalInformation().getGender());
        assertThat(response.getAddress().getCity()).isEqualTo(member.getAddress().getCity());
        assertThat(response.getAddress().getAddress()).isEqualTo(member.getAddress().getAddress());
        assertThat(response.getAddress().getZipcode()).isEqualTo(member.getAddress().getZipcode());
    }

    @Test
    @DisplayName("회원 조회시 없을 경우 테스트")
    void findMemberNotFoundTest() {
        // given
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        // when
        MemberException memberException = assertThrows(MemberException.class, () -> memberService.getMember("test"));

        // then
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.toString());
    }

    @Test
    @DisplayName("관리자용 회원 전체 조회 테스트")
    void getMemberListTest() {
        List<Member> members = new ArrayList<>();
        // given
        for (int i = 1; i <= 100; i++) {
            members.add(Member.builder()
                    .email("test@" + i)
                    .pass("pass" + i)

                    .personalInformation(PersonalInformation.builder()
                            .gender(Gender.MAN)
                            .birth(Birth.builder()
                                    .year("2022")
                                    .month("10")
                                    .day("20")
                                    .build())
                            .phoneNumber("010" + i)
                            .name("test" + i)
                            .build())
                    .address(Address.builder()
                            .city("서울")
                            .address("마포")
                            .zipcode("10121")
                            .build())
                    .build());
        }

        given(memberRepository.findAll())
                .willReturn(members);

        // when
        MemberListResponse response = memberService.getMemberList();

        // then
        assertThat(response.getTotalCount()).isEqualTo(100);
        assertThat(response.getMembers().get(0).getName()).isEqualTo("test1");
        assertThat(response.getMembers().get(99).getName()).isEqualTo("test100");
    }

    @Test
    @DisplayName("관리자용 회원 전체 조회 테스트(회원이 없을 경우 테스트)")
    void getMemberEmptyListTest() {

        given(memberRepository.findAll())
                .willReturn(List.of());

        // when
        MemberListResponse response = memberService.getMemberList();

        // then
        assertThat(response.getTotalCount()).isEqualTo(0);
    }

    
    private void createMemberDetailResponse() {
        MemberDetailResponse response = MemberDetailResponse.builder()
                .id(1L)
                .email("test@gamil.com")
                .info(PersonInformationRequest.builder()
                        .name("테스트")
                        .phoneNumber("01011112222")
                        .year("2022")
                        .month("10")
                        .day("20")
                        .gender(Gender.MAN)
                        .build())
                .address(Address.builder()
                        .city("서울시")
                        .address("마포구")
                        .zipcode("11111")
                        .build())
                .build();
    }
}