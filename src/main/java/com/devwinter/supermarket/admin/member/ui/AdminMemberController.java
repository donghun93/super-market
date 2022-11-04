package com.devwinter.supermarket.admin.member.ui;

import com.devwinter.supermarket.admin.member.command.application.MemberRoleService;
import com.devwinter.supermarket.admin.member.command.request.MemberRoleStatus;
import com.devwinter.supermarket.admin.member.query.application.AdminMemberQueryService;
import com.devwinter.supermarket.admin.member.query.response.AdminMemberDetailResponse;
import com.devwinter.supermarket.admin.role.command.domain.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminMemberQueryService adminMemberQueryService;
    private final MemberRoleService memberRoleService;

    @GetMapping
    public String getMemberList(Pageable pageable, Model model) {
        model.addAttribute("memberList", adminMemberQueryService.getMemberList(pageable));
        return "admin/member/manage";
    }

    @GetMapping("/{id}")
    public String getMemberDetail(@PathVariable Long id, Model model) {
        model.addAttribute("member", adminMemberQueryService.getMemberDetail(id));
        model.addAttribute("roles", adminMemberQueryService.getRoles());
        return "admin/member/detail";
    }

    @PostMapping("/update/role/{id}")
    public String updateMemberRole(@PathVariable("id") Long memberRoleId, Long roleId) {
        memberRoleService.changeMemberRole(memberRoleId, roleId);
        return "redirect:/admin/members";
    }

    @GetMapping("/block/{id}")
    public String memberBlock(@PathVariable("id") Long memberId) {
        memberRoleService.memberBlock(memberId);
        return "redirect:/admin/members";
    }
}
