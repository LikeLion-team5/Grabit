package com.ll.grabit.boundedcontext.member.controller;

import com.ll.grabit.boundedcontext.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String showJoin(Model model) {

        model.addAttribute("isJoinPage", true);

        return "usr/member/join";
    }

    @Getter
    @AllArgsConstructor
    public static class JoinForm {

        @NotBlank
        @Size(min = 4, max = 30)
        private final String username;

        @NotBlank
        @Size(min = 4, max = 30)
        private final String password;
    }

    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm) {

        memberService.join(joinForm.getUsername(), joinForm.getPassword());

        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLogin(Model model) {

        model.addAttribute("isLoginPage", true);

        return "usr/member/login";
    }
}