package com.devwinter.supermarket.member.service.impl;

import com.devwinter.supermarket.member.domain.Member;
import com.devwinter.supermarket.member.domain.Region;
import com.devwinter.supermarket.member.domain.type.RegionRange;
import com.devwinter.supermarket.member.domain.value.RegionLocation;
import com.devwinter.supermarket.member.exception.RegionException;
import com.devwinter.supermarket.member.repository.MemberRepository;
import com.devwinter.supermarket.member.repository.RegionRepository;
import com.devwinter.supermarket.member.request.RegionCreateRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.devwinter.supermarket.member.exception.RegionErrorCode.REGION_MAX_OVER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegionServiceImplTest {

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private RegionServiceImpl regionService;

    @Test
    @DisplayName("동네 등록 테스트")
    void registerRegionTest() {
        // given
        RegionCreateRequest request = RegionCreateRequest.builder()
                .memberId(1L)
                .name("공덕동")
                .latitude(37.232)
                .longitude(128.123)
                .build();
        Member member = Member.builder().build();

        ArgumentCaptor<Region> captor = ArgumentCaptor.forClass(Region.class);

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        // when
        regionService.registerRegion(request);
        

        // then
        verify(regionRepository).save(captor.capture());

        assertThat(captor.getValue().getName()).isEqualTo("공덕동");
        assertThat(captor.getValue().getMember()).isEqualTo(member);
        assertThat(captor.getValue().getLocation().getLongitude()).isEqualTo(128.123);
        assertThat(captor.getValue().getLocation().getLatitude()).isEqualTo(37.232);
    }

    @Test
    @DisplayName("동네 등록 초과 실패 테스트")
    void registerOverRegionTest() {
        // given
        RegionCreateRequest request = RegionCreateRequest.builder()
                .memberId(1L)
                .name("공덕동")
                .latitude(37.232)
                .longitude(128.123)
                .build();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder().build()));

        given(regionRepository.findByMember(any()))
                .willReturn(List.of(
                        Region.builder().build(),
                        Region.builder().build()));

        // when
        RegionException regionException = assertThrows(RegionException.class, () -> regionService.registerRegion(request));

        // then
        assertThat(regionException.getErrorCode()).isEqualTo(REGION_MAX_OVER.toString());
    }
    
    @Test
    @DisplayName("대표 동네 설정 테스트")
    void setLeadRegionTest() {
        // given
        RegionLocation regionLocation = RegionLocation.builder()
                .latitude(37.232)
                .longitude(128.321)
                .build();

        Member member = Member.builder()
                .build();

        Region region = Region.builder()
                .name("공덕동")
                .member(member)
                .location(regionLocation)
                .build();

        List<Region> lists = new ArrayList<>();
        Region region1 = Region.builder()
                .name("공덕동")
                .member(member)
                .location(regionLocation)
                .build();
        Region region2 = Region.builder()
                .name("공덕동2")
                .member(member)
                .location(regionLocation)
                .build();
        region1.changeLead(true);

        lists.add(region1);
        lists.add(region2);

        given(regionRepository.findWithMemberById(anyLong()))
                .willReturn(Optional.of(region));

        given(regionRepository.findByMember(any()))
                .willReturn(lists);

        // when

        regionService.setLeadRegion(anyLong());

        
        // then
        assertThat(region.getLeadYn()).isEqualTo(true);
        assertThat(region1.getLeadYn()).isEqualTo(false);
        assertThat(region2.getLeadYn()).isEqualTo(false);
    }
    
    @Test
    @DisplayName("동네 인증 테스트")
    void authRegionTest() {
        // given
        Region region = Region.builder()
                .name("공덕동")
                .member(Member.builder().build())
                .location(RegionLocation.builder().build())
                .build();

        given(regionRepository.findWithMemberById(anyLong()))
                .willReturn(Optional.of(region));
    
        // when
        regionService.authRegion(anyLong());
        
        // then
        assertThat(region.getAuthYn()).isEqualTo(true);
    }
    
    @ParameterizedTest
    @EnumSource(RegionRange.class)
    @DisplayName("동네 범위 설정 테스트")
    void setRegionRangeTest(RegionRange range) {
        // given
        Region region = Region.builder()
                .name("공덕동")
                .member(Member.builder().build())
                .location(RegionLocation.builder().build())
                .build();

        Region mock = spy(region);
        given(regionRepository.findWithMemberById(anyLong()))
                .willReturn(Optional.of(mock));

        // when
        regionService.setRegionRange(anyLong(), range);
        
        // then

        verify(regionRepository, times(1)).findWithMemberById(anyLong());
        verify(mock, times(1)).changeRange(any());
        assertThat(mock.getRange()).isEqualTo(range);
    }
    
    @Test
    @DisplayName("설정된 동네 삭제 테스트")
    void deleteRegionTest() {
        // given
        Region region = Region.builder()
                .name("공덕동")
                .member(Member.builder().build())
                .location(RegionLocation.builder().build())
                .build();

        Region mock = spy(region);
        given(regionRepository.findWithMemberById(anyLong()))
                .willReturn(Optional.of(mock));

        // when
        regionService.deleteRegion(anyLong());
        
        // then
        verify(regionRepository, times(1)).delete(mock);
    }
}