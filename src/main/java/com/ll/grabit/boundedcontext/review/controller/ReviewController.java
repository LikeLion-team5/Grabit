package com.ll.grabit.boundedcontext.review.controller;

import com.ll.grabit.base.rq.Rq;
import com.ll.grabit.base.rsdata.RsData;
import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.service.RestaurantService;
import com.ll.grabit.boundedcontext.review.entity.Review;
import com.ll.grabit.boundedcontext.review.form.AddReviewForm;
import com.ll.grabit.boundedcontext.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final RestaurantService restaurantService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add/{restaurantId}")
    public String showAddReview(@PathVariable Long restaurantId, Model model) {
        Restaurant restaurant = restaurantService.findOne(restaurantId);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("now", LocalDateTime.now());
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
    public String showList(Model model) {
        Member member = rq.getMember();

        if(member != null){
            List<Review> reviewList = reviewService.findByReviewerId(member.getId());

            model.addAttribute("reviewList", reviewList);
        }

        return "usr/review/check";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{reviewId}")
    public String showModify(@PathVariable Long reviewId, Model model) {
        Review review = reviewService.findById(reviewId).orElseThrow();
        model.addAttribute("review", review);

        return "usr/review/modify";
    }
}
