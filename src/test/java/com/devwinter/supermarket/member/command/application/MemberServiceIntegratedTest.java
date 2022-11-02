package com.devwinter.supermarket.member.command.application;

import com.devwinter.supermarket.member.command.exception.MemberException;
import com.devwinter.supermarket.member.command.application.request.MemberCreate;
import com.devwinter.supermarket.member.command.application.request.RegionCreate;
import com.devwinter.supermarket.member.command.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.UUID;

import static com.devwinter.supermarket.member.command.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ActiveProfiles({"testMemoryDB"})
public class MemberServiceIntegratedTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager em;


    @Test
    @DisplayName("회원 등록 테스트")
    void createMemberTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();

        // when
        Long memberId = memberService.createMember(memberCreate);

        // then
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("회원이 존재하지 않습니다."));

        assertThat(passwordEncoder.matches("12345", member.getPass().getValue())).isTrue();
        assertThat(member.getName()).isEqualTo("테스터");
        assertThat(member.getAddress().getAddress()).isEqualTo("주소1");
        assertThat(member.getAddress().getDetail()).isEqualTo("상세");
        assertThat(member.getAddress().getZipcode()).isEqualTo("123");
        assertThat(member.getGender()).isEqualTo(Gender.MAN);
    }

    private static MemberCreate getMemberCreate() {
        return MemberCreate.builder()
                .email(UUID.randomUUID().toString().substring(0,10))
                .password("12345")
                .name("테스터")
                .address(new Address("주소1", "상세", "123"))
                .gender(Gender.MAN)
                .build();
    }

    @Test
    @DisplayName("동네 추가 테스트")
    void createMemberRegionTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);
        RegionCreate regionCreate = RegionCreate.builder()
                .name("서울시")
                .latitude(32.132)
                .longitude(128.553)
                .build();

        // when
        memberService.createRegion(memberId, regionCreate);

        // then
        Member member = memberRepository.findWithRegionById(memberId)
                .orElseThrow(() -> new IllegalStateException("회원이 존재하지 않습니다."));

        assertThat(member.getRegions().size()).isEqualTo(1);
        assertThat(member.getRegions().get(0).getName()).isEqualTo("서울시");
        assertThat(member.getRegions().get(0).getLocation().getLatitude()).isEqualTo(32.132);
        assertThat(member.getRegions().get(0).getLocation().getLongitude()).isEqualTo(128.553);
        assertThat(member.getRegions().get(0).getRange()).isEqualTo(RegionRange.NORMAL);
    }

    @Test
    @DisplayName("동네 등록된 상태에서 새로운 동네 등록 테스트 (값타입 컬렉션 쿼리 확인용)")
    void createMemberNewRegionAddTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);
        RegionCreate regionCreate1 = RegionCreate.builder()
                .name("서울시")
                .latitude(32.132)
                .longitude(128.553)
                .build();
        memberService.createRegion(memberId, regionCreate1);
        RegionCreate regionCreate2 = RegionCreate.builder()
                .name("서울시2")
                .latitude(32.1325)
                .longitude(128.5531)
                .build();
        // when
        memberService.createRegion(memberId, regionCreate2);

        // then
        Member member = memberRepository.findWithRegionById(memberId)
                .orElseThrow(() -> new IllegalStateException("회원이 존재하지 않습니다."));
        assertThat(member.getRegions().size()).isEqualTo(2);
        assertThat(member.getRegions().get(0).getName()).isEqualTo("서울시");
        assertThat(member.getRegions().get(0).getLocation().getLatitude()).isEqualTo(32.132);
        assertThat(member.getRegions().get(0).getLocation().getLongitude()).isEqualTo(128.553);
        assertThat(member.getRegions().get(0).getRange()).isEqualTo(RegionRange.NORMAL);
    }
    
    @Test
    @DisplayName("동네 인증 테스트")
    void authRegionMemberTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);
        RegionCreate regionCreate = RegionCreate.builder()
                .name("서울시")
                .latitude(32.132)
                .longitude(128.553)
                .build();
        memberService.createRegion(memberId, regionCreate);


        // when
        memberService.regionAuth(memberId, 0);

        em.flush();
        em.clear();

        // then
        System.out.println("------------------");
        Member member = memberRepository.findWithRegionById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        assertThat(member.getRegions().get(0).isAuthStatus()).isTrue();
    }

    @Test
    @DisplayName("동네 등록이 여러개일 경우 하나만 인증 테스트")
    @Rollback(false)
    void authRegionMemberTest2() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);
        RegionCreate regionCreate1 = RegionCreate.builder()
                .name("서울시")
                .latitude(32.132)
                .longitude(128.553)
                .build();
        memberService.createRegion(memberId, regionCreate1);
        RegionCreate regionCreate2 = RegionCreate.builder()
                .name("서울시2")
                .latitude(32.1321)
                .longitude(128.5532)
                .build();
        memberService.createRegion(memberId, regionCreate2);

        // when
        memberService.regionAuth(memberId, 1);

        em.flush();
        em.clear();

        // then
        System.out.println("------------------");
        Member member = memberRepository.findWithRegionById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        assertThat(member.getRegions().get(1).isAuthStatus()).isTrue();
    }

    @Test
    @DisplayName("대표 동네 설정 테스트")
    void memberLeadRegionTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);
        RegionCreate regionCreate1 = RegionCreate.builder()
                .name("서울시")
                .latitude(32.132)
                .longitude(128.553)
                .build();
        memberService.createRegion(memberId, regionCreate1);
        RegionCreate regionCreate2 = RegionCreate.builder()
                .name("서울시2")
                .latitude(32.1321)
                .longitude(128.5532)
                .build();
        memberService.createRegion(memberId, regionCreate2);

        // when
        memberService.changeLeadRegion(memberId, 1);

        em.flush();
        em.clear();

        // then
        System.out.println("------------------");
        Member member = memberRepository.findWithRegionById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        assertThat(member.getRegions().get(0).isLeadStatus()).isFalse();
        assertThat(member.getRegions().get(1).isLeadStatus()).isTrue();
    }

    @Test
    @DisplayName("대표 동네가 이미 설정된 경우 다른 동네로 변경할때 테스트")
    void memberLeadRegionChangeTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);
        RegionCreate regionCreate1 = RegionCreate.builder()
                .name("서울시")
                .latitude(32.132)
                .longitude(128.553)
                .build();
        memberService.createRegion(memberId, regionCreate1);
        RegionCreate regionCreate2 = RegionCreate.builder()
                .name("서울시2")
                .latitude(32.1321)
                .longitude(128.5532)
                .build();
        memberService.createRegion(memberId, regionCreate2);
        memberService.changeLeadRegion(memberId, 0);

        // when
        memberService.changeLeadRegion(memberId, 1);

        em.flush();
        em.clear();

        // then
        System.out.println("------------------");
        Member member = memberRepository.findWithRegionById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        assertThat(member.getRegions().get(0).isLeadStatus()).isFalse();
        assertThat(member.getRegions().get(1).isLeadStatus()).isTrue();
    }
    
    @Test
    @DisplayName("회원 삭제 테스트")
    void memberDeleteTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);

        // when
        memberService.deleteMember(memberId);

        // then
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("회원이 존재하지 않습니다."));

        assertThat(passwordEncoder.matches("12345", member.getPass().getValue())).isTrue();
        assertThat(member.getName()).isEqualTo("테스터");
        assertThat(member.getAddress().getAddress()).isEqualTo("주소1");
        assertThat(member.getAddress().getDetail()).isEqualTo("상세");
        assertThat(member.getAddress().getZipcode()).isEqualTo("123");
        assertThat(member.getGender()).isEqualTo(Gender.MAN);
        assertThat(member.getDeleteYn()).isTrue();
    }

    @Test
    @DisplayName("동네 삭제 테스트")
    void deleteRegionTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);
        RegionCreate regionCreate = RegionCreate.builder()
                .name("서울시")
                .latitude(32.132)
                .longitude(128.553)
                .build();
        memberService.createRegion(memberId, regionCreate);

        // when
        memberService.deleteRegion(memberId, 0);

        em.flush();
        em.clear();

        // then
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("회원이 존재하지 않습니다."));

        assertThat(member.getRegions().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("동네 여러개 등록 중 하나만 삭제한 경우 테스트")
    void multiRegisterDeleteRegionTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);
        RegionCreate regionCreate1 = RegionCreate.builder()
                .name("서울시")
                .latitude(32.132)
                .longitude(128.553)
                .build();
        memberService.createRegion(memberId, regionCreate1);
        RegionCreate regionCreate2 = RegionCreate.builder()
                .name("서울시2")
                .latitude(32.1322)
                .longitude(128.5532)
                .build();
        memberService.createRegion(memberId, regionCreate2);

        // when
        memberService.deleteRegion(memberId, 1);

        em.flush();
        em.clear();

        // then
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("회원이 존재하지 않습니다."));

        assertThat(member.getRegions().size()).isEqualTo(1);
        assertThat(member.getRegions().get(0).getName()).isEqualTo("서울시");
        assertThat(member.getRegions().get(0).getLocation().getLatitude()).isEqualTo(32.132);
        assertThat(member.getRegions().get(0).getLocation().getLongitude()).isEqualTo(128.553);
    }
}
