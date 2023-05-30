package com.ll.grabit.boundedcontext.member.controller;

import com.ll.grabit.base.rsdata.RsData;
import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.member.form.MemberCreateDto;
import com.ll.grabit.boundedcontext.member.service.MemberService;
import com.ll.grabit.standard.util.Ut;
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
            return "common/js";

        }

        return "redirect:/member/login?msg=" + Ut.url.encode("회원가입이 완료되었습니다.\n로그인 후 이용해주세요.");

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