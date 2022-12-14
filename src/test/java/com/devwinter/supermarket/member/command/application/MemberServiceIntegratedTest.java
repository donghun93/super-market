package com.devwinter.supermarket.member.command.application;

import com.devwinter.supermarket.admin.role.command.application.RoleService;
import com.devwinter.supermarket.admin.role.command.domain.Role;
import com.devwinter.supermarket.admin.role.command.domain.RoleRepository;
import com.devwinter.supermarket.member.command.exception.MemberException;
import com.devwinter.supermarket.member.command.application.request.MemberCreate;
import com.devwinter.supermarket.member.command.application.request.RegionCreate;
import com.devwinter.supermarket.member.command.domain.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.jdbc.Sql;
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
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void before() {
        roleRepository.save(Role.builder()
                .name("ROLE_ADMIN")
                .desc("?????????")
                .build());

        roleRepository.save(Role.builder()
                .name("ROLE_SYS")
                .desc("????????? ?????????")
                .build());

        roleRepository.save(Role.builder()
                .name("ROLE_USER")
                .desc("?????????")
                .build());
    }

    @AfterEach
    void after() {
        roleRepository.deleteAll();
    }


    @Test
    @DisplayName("?????? ?????? ?????????")
    void createMemberTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();

        // when
        Long memberId = memberService.createMember(memberCreate);

        // then
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("????????? ???????????? ????????????."));

        assertThat(passwordEncoder.matches("12345", member.getPass().getValue())).isTrue();
        assertThat(member.getName()).isEqualTo("?????????");
        assertThat(member.getAddress().getAddress()).isEqualTo("??????1");
        assertThat(member.getAddress().getDetail()).isEqualTo("??????");
        assertThat(member.getAddress().getZipcode()).isEqualTo("123");
        assertThat(member.getGender()).isEqualTo(Gender.MAN);
    }

    private static MemberCreate getMemberCreate() {
        return MemberCreate.builder()
                .email(UUID.randomUUID().toString().substring(0, 10))
                .password("12345")
                .name("?????????")
                .address("??????1")
                .detail("??????")
                .zipcode("123")
                .gender(Gender.MAN)
                .build();
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    void createMemberRegionTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);
        RegionCreate regionCreate = RegionCreate.builder()
                .name("?????????")
                .latitude(32.132)
                .longitude(128.553)
                .build();

        // when
        memberService.createRegion(memberId, regionCreate);

        // then
        Member member = memberRepository.findWithRegionById(memberId)
                .orElseThrow(() -> new IllegalStateException("????????? ???????????? ????????????."));

        assertThat(member.getRegions().size()).isEqualTo(1);
        assertThat(member.getRegions().get(0).getName()).isEqualTo("?????????");
        assertThat(member.getRegions().get(0).getLocation().getLatitude()).isEqualTo(32.132);
        assertThat(member.getRegions().get(0).getLocation().getLongitude()).isEqualTo(128.553);
        assertThat(member.getRegions().get(0).getRange()).isEqualTo(RegionRange.NORMAL);
    }

    @Test
    @DisplayName("?????? ????????? ???????????? ????????? ?????? ?????? ????????? (????????? ????????? ?????? ?????????)")
    void createMemberNewRegionAddTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);
        RegionCreate regionCreate1 = RegionCreate.builder()
                .name("?????????")
                .latitude(32.132)
                .longitude(128.553)
                .build();
        memberService.createRegion(memberId, regionCreate1);
        RegionCreate regionCreate2 = RegionCreate.builder()
                .name("?????????2")
                .latitude(32.1325)
                .longitude(128.5531)
                .build();
        // when
        memberService.createRegion(memberId, regionCreate2);

        // then
        Member member = memberRepository.findWithRegionById(memberId)
                .orElseThrow(() -> new IllegalStateException("????????? ???????????? ????????????."));
        assertThat(member.getRegions().size()).isEqualTo(2);
        assertThat(member.getRegions().get(0).getName()).isEqualTo("?????????");
        assertThat(member.getRegions().get(0).getLocation().getLatitude()).isEqualTo(32.132);
        assertThat(member.getRegions().get(0).getLocation().getLongitude()).isEqualTo(128.553);
        assertThat(member.getRegions().get(0).getRange()).isEqualTo(RegionRange.NORMAL);
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    void authRegionMemberTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);
        RegionCreate regionCreate = RegionCreate.builder()
                .name("?????????")
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
    @DisplayName("?????? ????????? ???????????? ?????? ????????? ?????? ?????????")
    void authRegionMemberTest2() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);
        RegionCreate regionCreate1 = RegionCreate.builder()
                .name("?????????")
                .latitude(32.132)
                .longitude(128.553)
                .build();
        memberService.createRegion(memberId, regionCreate1);
        RegionCreate regionCreate2 = RegionCreate.builder()
                .name("?????????2")
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
    @DisplayName("?????? ?????? ?????? ?????????")
    void memberLeadRegionTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);
        RegionCreate regionCreate1 = RegionCreate.builder()
                .name("?????????")
                .latitude(32.132)
                .longitude(128.553)
                .build();
        memberService.createRegion(memberId, regionCreate1);
        RegionCreate regionCreate2 = RegionCreate.builder()
                .name("?????????2")
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
    @DisplayName("?????? ????????? ?????? ????????? ?????? ?????? ????????? ???????????? ?????????")
    void memberLeadRegionChangeTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);
        RegionCreate regionCreate1 = RegionCreate.builder()
                .name("?????????")
                .latitude(32.132)
                .longitude(128.553)
                .build();
        memberService.createRegion(memberId, regionCreate1);
        RegionCreate regionCreate2 = RegionCreate.builder()
                .name("?????????2")
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
    @DisplayName("?????? ?????? ?????????")
    void memberDeleteTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);

        // when
        memberService.deleteMember(memberId);

        // then
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("????????? ???????????? ????????????."));

        assertThat(passwordEncoder.matches("12345", member.getPass().getValue())).isTrue();
        assertThat(member.getName()).isEqualTo("?????????");
        assertThat(member.getAddress().getAddress()).isEqualTo("??????1");
        assertThat(member.getAddress().getDetail()).isEqualTo("??????");
        assertThat(member.getAddress().getZipcode()).isEqualTo("123");
        assertThat(member.getGender()).isEqualTo(Gender.MAN);
        assertThat(member.getDeleteYn()).isTrue();
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    void deleteRegionTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);
        RegionCreate regionCreate = RegionCreate.builder()
                .name("?????????")
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
                .orElseThrow(() -> new IllegalStateException("????????? ???????????? ????????????."));

        assertThat(member.getRegions().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("?????? ????????? ?????? ??? ????????? ????????? ?????? ?????????")
    void multiRegisterDeleteRegionTest() {
        // given
        MemberCreate memberCreate = getMemberCreate();
        Long memberId = memberService.createMember(memberCreate);
        RegionCreate regionCreate1 = RegionCreate.builder()
                .name("?????????")
                .latitude(32.132)
                .longitude(128.553)
                .build();
        memberService.createRegion(memberId, regionCreate1);
        RegionCreate regionCreate2 = RegionCreate.builder()
                .name("?????????2")
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
                .orElseThrow(() -> new IllegalStateException("????????? ???????????? ????????????."));

        assertThat(member.getRegions().size()).isEqualTo(1);
        assertThat(member.getRegions().get(0).getName()).isEqualTo("?????????");
        assertThat(member.getRegions().get(0).getLocation().getLatitude()).isEqualTo(32.132);
        assertThat(member.getRegions().get(0).getLocation().getLongitude()).isEqualTo(128.553);
    }
}
