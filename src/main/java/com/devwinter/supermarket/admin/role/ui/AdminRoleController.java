package com.devwinter.supermarket.admin.role.ui;


import com.devwinter.supermarket.admin.role.command.application.RoleService;
import com.devwinter.supermarket.admin.role.command.application.request.RoleCreate;
import com.devwinter.supermarket.admin.role.command.application.request.RoleUpdate;
import com.devwinter.supermarket.admin.role.query.application.RoleQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/admin/authorize/roles")
@RequiredArgsConstructor
public class AdminRoleController {

    private final RoleService roleService;
    private final RoleQueryService roleQueryService;

    @GetMapping
    public String getRoles(Pageable pageable, Model model) {
        model.addAttribute("items", roleQueryService.getRoleList(pageable));
        return "admin/authorize/role/manage";
    }

    @GetMapping("/register")
    public String createRole(Model model) {
        model.addAttribute("roleCreate", new RoleCreate());
        return "admin/authorize/role/register";
    }

    @PostMapping("/register")
    public String createRoleSubmit(@Valid RoleCreate roleCreate, BindingResult result) {

        if(result.hasErrors()) {
            return "admin/authorize/role/register";
        }
        roleService.createRole(roleCreate);
        return "redirect:/admin/authorize/roles";
    }

    @GetMapping("/{id}")
    public String getRoleDetail(@PathVariable Long id, Model model) {

        model.addAttribute("roleUpdate", roleQueryService.getRoleDetail(id));
        return "admin/authorize/role/detail";
    }

    @DeleteMapping("/{id}")
    public String deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return "redirect:/admin/authorize/roles";
    }

    @PostMapping("/update")
    public String updateRole(@ModelAttribute @Valid RoleUpdate roleUpdate, BindingResult result) {

        if(result.hasErrors()) {
            return "admin/authorize/role/detail";
        }

        roleService.updateRole(roleUpdate);
        return "redirect:/admin/authorize/roles";
    }
}
