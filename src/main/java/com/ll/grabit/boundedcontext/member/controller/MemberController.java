package com.ll.grabit.boundedcontext.member.controller;

import com.ll.grabit.base.rq.Rq;
import com.ll.grabit.base.rsdata.RsData;
import com.ll.grabit.boundedcontext.member.dto.MemberEditDto;
import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.member.form.MemberCreateDto;
import com.ll.grabit.boundedcontext.member.service.MemberService;
import com.ll.grabit.base.standard.util.Ut;
import com.ll.grabit.boundedcontext.reservation.dto.ReservationResponseDto;
import com.ll.grabit.boundedcontext.reservation.entity.Reservation;
import com.ll.grabit.boundedcontext.reservation.service.ReservationService;
import com.ll.grabit.boundedcontext.review.entity.Review;
import com.ll.grabit.boundedcontext.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ReviewService reviewService;
    private final ReservationService reservationService;

    private final Rq rq;


    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin(Model model) {

        return "usr/member/join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute MemberCreateDto memberCreateDto) {
        RsData<Member> rsData = memberService.join(memberCreateDto);

        if (rsData.isFail()) {
            return "common/js";
        }

        return "redirect:/member/login?msg=" + Ut.url.encode("회원가입이 완료되었습니다.\n로그인 후 이용해주세요.");

    }

    @GetMapping("/myInfo")
    public String showMyInfo(Model model) {
        if(rq.isLogout()){
            return rq.historyBack("로그인이 필요합니다.");
        }
        Member member = memberService.findByUsername(rq.getMember().getUsername()).get();
        List<Review> reviewList = reviewService.findByReviewerId(member.getId());
        List<ReservationResponseDto> reservationList = reservationService.getReservationsByMemberId(member.getId());

        LocalDateTime now = LocalDateTime.now();
        reservationList.sort((r1, r2) -> {
            LocalDateTime d1 = r1.getDate().atTime(r1.getReservationTime());
            LocalDateTime d2 = r2.getDate().atTime(r2.getReservationTime());

            boolean isR1Past = d1.isBefore(now);
            boolean isR2Past = d2.isBefore(now);

            if (isR1Past && !isR2Past) {
                return 1;
            } else if (!isR1Past && isR2Past) {
                return -1;
            } else {
                return d1.compareTo(d2);
            }
        });

        model.addAttribute("userInfo",member);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("reservations", reservationList);

        return "usr/member/myInfo";
    }

    @GetMapping("/login")
    public String showLogin(Model model) {

        return "usr/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/editInfo")
    public String showEditInfo(Model model) {

        Member member = memberService.findByUsername((rq.getMember().getUsername())).get();

        MemberEditDto memberEditDto = MemberEditDto.builder()
                .username(member.getUsername())
                .email(member.getEmail())
                .phone(member.getPhone())
                .nickname(member.getNickname())
                .build();
        model.addAttribute("editInfo", memberEditDto);

        return "usr/member/editInfo";
    }

    @PostMapping("/editInfo")
    public String editInfo(MemberEditDto memberEditDto) {
        RsData<Member> rsData = memberService.edit(memberEditDto, rq.getMember().getId());

        if (rsData.isFail()) {
            return "common/js";
        }

        return "redirect:/member/myInfo";
    }
}