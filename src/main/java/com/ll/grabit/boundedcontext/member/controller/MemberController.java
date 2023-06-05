package com.ll.grabit.boundedcontext.member.controller;

import com.ll.grabit.base.rq.Rq;
import com.ll.grabit.base.rsdata.RsData;
import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.member.form.MemberCreateDto;
import com.ll.grabit.boundedcontext.member.service.MemberService;
<<<<<<< HEAD
import com.ll.grabit.base.standard.util.Ut;
=======
import com.ll.grabit.standard.util.Ut;
>>>>>>> 0e001bbe008ed6caa0b8c9bb11661d290c318722
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final Rq rq;


    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin(Model model) {

        model.addAttribute("isJoinPage", true);

        return "usr/member/join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute MemberCreateDto memberCreateDto) {
        memberCreateDto.setPhone(memberCreateDto.getPhone());
        RsData<Member> rsData = memberService.join(memberCreateDto);

        if (rsData.isFail()) {
            return "common/js";
        }

        return "redirect:/member/login?msg=" + Ut.url.encode("회원가입이 완료되었습니다.\n로그인 후 이용해주세요.");

    }

    @GetMapping("/myInfo")
    public String showMe(Model model) {
        if(rq.isLogout()){
            return rq.historyBack("로그인이 필요합니다.");
        }
        Member member = memberService.findByUsername(rq.getMember().getUsername()).get();

        model.addAttribute("userInfo",member);

        return "usr/member/myInfo";
    }

    @GetMapping("/login")
    public String showLogin(Model model) {

        model.addAttribute("isLoginPage", true);

        return "usr/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/editInfo")
    public String showEditInfo() {
        return "usr/member/editInfo";
    }
}