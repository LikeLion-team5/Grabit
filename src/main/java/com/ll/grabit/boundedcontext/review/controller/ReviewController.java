package com.ll.grabit.boundedcontext.review.controller;

import com.ll.grabit.base.rq.Rq;
import com.ll.grabit.base.rsdata.RsData;
import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.review.entity.Review;
import com.ll.grabit.boundedcontext.review.form.AddReviewForm;
import com.ll.grabit.boundedcontext.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add/{restaurantId}")
    public String showAddReview(@PathVariable Long restaurantId) {
        return "usr/review/add";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add/{restaurantId}")
    public String addReview(@PathVariable Long restaurantId, AddReviewForm addReviewForm) {
        RsData<Review> rsData = reviewService.addReview(addReviewForm.getContent(), addReviewForm.getRating(), restaurantId, rq.getMember().getId());

        if (rsData.isFail()) {
            return rq.historyBack(rsData.getMsg());
        }

        return "redirect:/reservation/check";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/check")
    public String showReview(Model model) {
        Member member = rq.getMember();

        if(member != null){
            List<Review> reviewList = reviewService.findByReviewerId(member.getId());

            System.out.println("aaaa" + reviewList);

            model.addAttribute("reviewList", reviewList);
        }

        return "usr/review/check";
    }
}
