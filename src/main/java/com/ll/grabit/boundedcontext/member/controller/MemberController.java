package com.ll.grabit.boundedcontext.member.controller;

import com.ll.grabit.base.rsdata.RsData;
import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.member.form.MemberCreateDto;
import com.ll.grabit.boundedcontext.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {

        return "usr/member/join";
    }




    @PostMapping("/join")
    public String join(@Valid MemberCreateDto memberCreateDto) {

        RsData<Member> rsData = memberService.join(memberCreateDto);

        if (rsData.isFail()) {

        }

        return "usr/member/login";
    }
    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {
        return "usr/member/login";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String showMe() {
        return "usr/member/mypage";
    }
}