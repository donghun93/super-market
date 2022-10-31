package com.devwinter.supermarket.member.command.application;

import com.devwinter.supermarket.member.command.application.exception.MemberException;
import com.devwinter.supermarket.member.command.application.impl.MemberServiceImpl;
import com.devwinter.supermarket.member.command.application.request.MemberCreate;
import com.devwinter.supermarket.member.command.application.request.RegionCreate;
import com.devwinter.supermarket.member.command.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.devwinter.supermarket.member.command.application.exception.MemberErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("회원중복 가입 실패 테스트")
    void createMemberDuplicateTest() {
        // given
        MemberCreate memberCreate = MemberCreate.builder()
                .email("test@gmail.com")
                .password("12345")
                .name("테스터")
                .address(new Address("서울시", "상세", "123"))
                .gender(Gender.MAN)
                .build();

        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.of(Member.builder().build()));

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.createMember(memberCreate));

        // then
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_DUPLICATE_ERROR.toString());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_DUPLICATE_ERROR.getDescription());
    }

    @Test
    @DisplayName("회원가입 테스트")
    void createMemberTest() {
        // given
        MemberCreate memberCreate = MemberCreate.builder()
                .email("test@gmail.com")
                .password("12345")
                .name("테스터")
                .address(new Address("서울시", "마포구", "123"))
                .gender(Gender.MAN)
                .build();

        MemberCreate mockMemberCreate = spy(memberCreate);
        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.empty());

        given(passwordEncoder.encode(anyString()))
                .willReturn("54321");

        // when
        memberService.createMember(mockMemberCreate);

        // then
        verify(memberRepository, times(1)).findByEmail(any());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(mockMemberCreate, times(1)).toEntity(anyString());
        verify(memberRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getEmail().getValue()).isEqualTo("test@gmail.com");
        assertThat(captor.getValue().getPass().getValue()).isEqualTo("54321");
        assertThat(captor.getValue().getName()).isEqualTo("테스터");
        assertThat(captor.getValue().getAddress().getAddress()).isEqualTo("서울시");
        assertThat(captor.getValue().getAddress().getDetail()).isEqualTo("마포구");
        assertThat(captor.getValue().getAddress().getZipcode()).isEqualTo("123");
        assertThat(captor.getValue().getGender()).isEqualTo(Gender.MAN);
    }

    @Test
    @DisplayName("동네추가 시 회원이 존재하지 않을경우 테스트")
    void createRegionNotFoundTest() {
        // given
        given(memberRepository.findWithRegionById(any()))
                .willReturn(Optional.empty());

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.createRegion(1L, any()));

        // then
        verify(memberRepository, times(1)).findWithRegionById(any());
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.toString());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("동네추가 시 최대 등록 수 초과 테스트")
    void createRegionMaxSizeOverTest() {
        // given
        RegionCreate regionCreate = new RegionCreate("서울시", 32.132, 128.135);

        Member member = Member.builder().build();
        Member mockMember = spy(member);

        List<Region> lists = new ArrayList<>();
        for (int i = 0; i < Member.MAX_REGION_SIZE + 1; i++) {
            lists.add(new Region("테스트", null));
        }

        given(memberRepository.findWithRegionById(any()))
                .willReturn(Optional.of(mockMember));
        given(mockMember.getRegions()).willReturn(lists);

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.createRegion(1L, regionCreate));

        // then
        verify(memberRepository, times(1)).findWithRegionById(any());
        verify(mockMember, times(1)).createRegion(any());
        verify(mockMember, times(1)).getRegions();
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_REGION_MAX.toString());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_REGION_MAX.getDescription());
    }

    @Test
    @DisplayName("동네추가 시 동네명 중복 테스트")
    void createRegionNameDuplicateTest() {
        // given
        String regionName = "서울시";
        RegionCreate regionCreate = new RegionCreate(regionName, 32.132, 128.135);

        Member member = Member.builder().build();
        Member mockMember = spy(member);
        List<Region> lists = List.of(new Region(regionName, null));


        given(memberRepository.findWithRegionById(any()))
                .willReturn(Optional.of(mockMember));
        given(mockMember.getRegions())
                .willReturn(lists);

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.createRegion(1L, regionCreate));

        // then
        verify(memberRepository, times(1)).findWithRegionById(any());
        verify(mockMember, times(2)).getRegions();
        verify(mockMember, times(1)).createRegion(any());
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_ALREADY_REGION_NAME.toString());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_ALREADY_REGION_NAME.getDescription());
    }

    @Test
    @DisplayName("동네추가 시 동네위치 중복 테스트")
    void createRegionLocationDuplicateTest() {
        // given
        Double latitude = 32.132;
        Double longitude = 128.135;
        RegionCreate regionCreate = new RegionCreate("서울시1", latitude, longitude);

        Member member = Member.builder().build();
        Member mockMember = spy(member);
        List<Region> lists = List.of(new Region("서울시2", new Location(latitude, longitude)));


        given(memberRepository.findWithRegionById(any()))
                .willReturn(Optional.of(mockMember));
        given(mockMember.getRegions())
                .willReturn(lists);

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.createRegion(1L, regionCreate));

        // then
        verify(memberRepository, times(1)).findWithRegionById(any());
        verify(mockMember, times(2)).getRegions();
        verify(mockMember, times(1)).createRegion(any());
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_ALREADY_REGION_REGISTER.toString());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_ALREADY_REGION_REGISTER.getDescription());
    }
    
    @Test
    @DisplayName("동네추가 테스트")
    void createRegionTest() {
        // given
        RegionCreate regionCreate = new RegionCreate("서울시1", 32.132, 125.332);

        List<Region> lists = new ArrayList<>();

        Member member = Member.builder().build();
        Member mockMember = spy(member);

        given(memberRepository.findWithRegionById(any()))
                .willReturn(Optional.of(mockMember));
        given(mockMember.getRegions())
                .willReturn(lists);
    
        // when
        memberService.createRegion(1L, regionCreate);
        
        // then
        ArgumentCaptor<Region> captor = ArgumentCaptor.forClass(Region.class);

        verify(mockMember, times(1)).createRegion(captor.capture());
        verify(memberRepository, times(1)).findWithRegionById(any());
        assertThat(mockMember.getRegions().size()).isEqualTo(1);
        assertThat(captor.getValue().getName()).isEqualTo("서울시1");
        assertThat(captor.getValue().getRange()).isEqualTo(RegionRange.NORMAL);
        assertThat(captor.getValue().getLocation().getLatitude()).isEqualTo(32.132);
        assertThat(captor.getValue().getLocation().getLongitude()).isEqualTo(125.332);
    }

    @Test
    @DisplayName("동네인증 시 회원 못찾은 경우 테스트")
    void regionAuthMemberNotFoundTest() {
        // given
        given(memberRepository.findWithRegionById(any()))
                .willReturn(Optional.empty());

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.regionAuth(1L, anyInt()));

        // then
        verify(memberRepository, times(1)).findWithRegionById(any());
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.toString());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("동네 인증시 인덱스 번호 최대 동네 수 넘었을경우 테스트")
    void regionAuthNotValidIdxMaxRegionCountOverTest() {
        // given

        Member member = Member.builder().build();
        given(memberRepository.findWithRegionById(any()))
                .willReturn(Optional.of(member));
    
        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.regionAuth(1L, Member.MAX_REGION_SIZE + 1));

        //then
        verify(memberRepository, times(1)).findWithRegionById(any());
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_REGION_IDX_NOT_VALID.toString());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_REGION_IDX_NOT_VALID.getDescription());
    }

    @Test
    @DisplayName("동네 인증시 인덱스 번호 리스트 수 넘었을경우 테스트")
    void regionAuthNotValidIdxListCountOverTest() {
        // given

        Member member = Member.builder().build();
        Member mockMember = spy(member);
        given(mockMember.getRegions())
                .willReturn(List.of(new Region("테스트", null)));
        given(memberRepository.findWithRegionById(any()))
                .willReturn(Optional.of(mockMember));

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.regionAuth(1L, mockMember.getRegions().size() + 1));

        //then
        verify(memberRepository, times(1)).findWithRegionById(any());
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_REGION_IDX_NOT_VALID.toString());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_REGION_IDX_NOT_VALID.getDescription());
    }
    
    @Test
    @DisplayName("동네인증 테스트")
    void regionAuthTest() {
        // given

        Member member = Member.builder().build();
        Member mockMember = spy(member);
        Region mockRegion = spy(new Region("테스트", null));

        given(mockMember.getRegions())
                .willReturn(List.of(mockRegion));
        given(memberRepository.findWithRegionById(any()))
                .willReturn(Optional.of(mockMember));

        // when
        memberService.regionAuth(1L, 0);

        //then
        verify(memberRepository, times(1)).findWithRegionById(any());
        verify(mockMember, times(1)).getRegions();
        verify(mockRegion, times(1)).authStatusSuccess();
        assertThat(mockRegion.isAuthStatus()).isTrue();
    }
    
    @Test
    @DisplayName("대표동네 설정 시 회원없을 경우 테스트")
    void changeLeadRegionMemberNotFoundTest() {
        // given
        given(memberRepository.findWithRegionById(any()))
                .willReturn(Optional.empty());

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.changeLeadRegion(1L, anyInt()));

        // then
        verify(memberRepository, times(1)).findWithRegionById(any());
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.toString());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("대표동네 설정 시 인덱스 번호 최대 동네 수 넘었을경우 테스트")
    void changeLeadRegionNotValidIdxMaxRegionCountOverTest() {
        // given
        Member member = Member.builder().build();
        given(memberRepository.findWithRegionById(any()))
                .willReturn(Optional.of(member));

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.changeLeadRegion(1L, Member.MAX_REGION_SIZE + 1));

        //then
        verify(memberRepository, times(1)).findWithRegionById(any());
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_REGION_IDX_NOT_VALID.toString());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_REGION_IDX_NOT_VALID.getDescription());
    }

    @Test
    @DisplayName("대표동네 설정 시 인덱스 번호 리스트 수 넘었을경우 테스트")
    void changeLeadRegionNotValidIdxListCountOverTest() {
        // given
        Member member = Member.builder().build();
        Member mockMember = spy(member);
        given(mockMember.getRegions())
                .willReturn(List.of(new Region("테스트", null)));
        given(memberRepository.findWithRegionById(any()))
                .willReturn(Optional.of(mockMember));

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.changeLeadRegion(1L, mockMember.getRegions().size() + 1));

        //then
        verify(memberRepository, times(1)).findWithRegionById(any());
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_REGION_IDX_NOT_VALID.toString());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_REGION_IDX_NOT_VALID.getDescription());
    }

    @Test
    @DisplayName("대표동네 설정 테스트")
    void changeLeadRegionTest() {
        // given
        Member member = Member.builder().build();
        List<Region> regions = new ArrayList<>();
        regions.add(new Region("서울시1", null));
        regions.add(new Region("서울시2", null));
        regions.get(0).changeLeadStatus(true);
        regions.get(1).changeLeadStatus(true);


        Member mockMember = spy(member);

        given(memberRepository.findWithRegionById(any()))
                .willReturn(Optional.of(mockMember));
        given(mockMember.getRegions())
                .willReturn(regions);

        // when
        memberService.changeLeadRegion(1L, 1);

        // then
        verify(mockMember, times(2)).getRegions();
        assertThat(regions.get(0).isLeadStatus()).isFalse();
        assertThat(regions.get(1).isLeadStatus()).isTrue();
    }
    

    @Test
    @DisplayName("회원탈퇴 시 회원조회 실패 테스트")
    void deleteMemberNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.deleteMember(1L));

        // then
        verify(memberRepository, times(1)).findById(anyLong());
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_NOT_FOUND.toString());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("회원탈퇴 테스트")
    void deleteMemberTest() {
        // given
        Member mockMember = spy(Member.builder().build());
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(mockMember));

        // when
        memberService.deleteMember(1L);

        // then
        verify(memberRepository, times(1)).findById(anyLong());
        verify(mockMember, times(1)).deleteMember();
        assertThat(mockMember.isDeleteYn()).isTrue();
    }
    
    @Test
    @DisplayName("동네 삭제 테스트")
    void deleteRegionTest() {
        // given
        Member mockMember = spy(Member.builder().build());
        List<Region> regions = new ArrayList<>();
        regions.add(new Region("서울시1", null));
        regions.add(new Region("서울시2", null));
        regions.get(0).changeLeadStatus(true);
        regions.get(1).changeLeadStatus(true);

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(mockMember));
        given(mockMember.getRegions())
                .willReturn(regions);

        // when
        memberService.deleteRegion(1L, 1);
        
        // then
        verify(memberRepository, times(1)).findById(anyLong());
        assertThat(mockMember.getRegions().size()).isEqualTo(1);
    }

}