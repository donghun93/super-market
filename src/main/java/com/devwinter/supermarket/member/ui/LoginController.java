package com.devwinter.supermarket.member.ui;

import com.devwinter.supermarket.member.command.application.MemberService;
import com.devwinter.supermarket.member.command.application.request.MemberCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        String uri = request.getHeader("Referer");
        if (uri != null && !uri.contains("/login")) {
            request.getSession().setAttribute("prevPage", uri);
        }
        return "login/login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new MemberCreate());
        return "login/signup";
    }

    @PostMapping("/signup")
    public String createMember(@Valid @ModelAttribute("user") MemberCreate memberCreate,
                               BindingResult result) {

        if(result.hasErrors()) {
            return "login/signup";
        }

        memberService.createMember(memberCreate);
        return "redirect:/";
    }
}
