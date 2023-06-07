package com.ll.grabit.boundedcontext.review.controller;

import com.ll.grabit.base.rq.Rq;
import com.ll.grabit.base.rsdata.RsData;
import com.ll.grabit.boundedcontext.review.entity.Review;
import com.ll.grabit.boundedcontext.review.form.AddReviewForm;
import com.ll.grabit.boundedcontext.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("restaurant/{restaurantId}/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add")
    public String showAddReview(@PathVariable Long restaurantId) {
        return "usr/review/add";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public String addReview(@PathVariable Long restaurantId, AddReviewForm addReviewForm) {
        RsData<Review> rsData = reviewService.addReview(addReviewForm.getContent(), addReviewForm.getRating(), restaurantId, rq.getMember().getId());

        if (rsData.isFail()) {
            return rq.historyBack(rsData.getMsg());
        }

        return "redirect:/reservation/check";
    }
}
