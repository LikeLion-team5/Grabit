package com.ll.grabit.boundedcontext.review.controller;

import com.ll.grabit.base.rq.Rq;
import com.ll.grabit.base.rsdata.RsData;
import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.reservation.entity.Reservation;
import com.ll.grabit.boundedcontext.reservation.service.ReservationService;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.service.RestaurantService;
import com.ll.grabit.boundedcontext.review.entity.Review;
import com.ll.grabit.boundedcontext.review.form.AddReviewForm;
import com.ll.grabit.boundedcontext.review.form.EditReviewForm;
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
    private final ReservationService reservationService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add/{reservationId}")
    public String showAddReview(@PathVariable Long reservationId, Model model) {
        Reservation reservation = reservationService.findByIdElseThrow(reservationId);
        model.addAttribute("restaurant", reservation.getRestaurant());
        model.addAttribute("now", LocalDateTime.now());
        return "usr/review/add";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add/{reservationId}")
    public String addReview(@PathVariable Long reservationId, AddReviewForm addReviewForm) {
        Reservation reservation = reservationService.findByIdElseThrow(reservationId);

        RsData<Review> rsData = reviewService.addReview(addReviewForm.getContent(), addReviewForm.getRating(), reservation, rq.getMember().getId());

        if (rsData.isFail()) {
            return rq.historyBack(rsData.getMsg());
        }

        return "redirect:/reservation/check";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/check")
    public String showList(Model model, @RequestParam(defaultValue = "1") int sortCode) {
        Member member = rq.getMember();

        if(member != null){
            RsData<List<Review>> reviewList = reviewService.getReviews(member.getId(), sortCode);
            model.addAttribute("reviewList", reviewList.getData());
        }

        return "usr/review/check";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit/{reviewId}")
    public String showEdit(@PathVariable Long reviewId, Model model) {
        RsData canModifyRsData = reviewService.canEdit(rq.getMember().getId(), reviewId);

        if (canModifyRsData.isFail()) return rq.historyBack(canModifyRsData.getMsg());

        model.addAttribute("review", canModifyRsData.getData());

        return "usr/review/edit";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit/{reviewId}")
    public String edit(@PathVariable Long reviewId, EditReviewForm form) {
        RsData<Review> rsData = reviewService.edit(rq.getMember().getId(), reviewId, form.getRating(), form.getContent());

        if (rsData.isFail()) {
            return rq.historyBack(rsData.getMsg());
        }

        return "redirect:/review/check";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{reviewId}")
    public String showDelete(@PathVariable Long reviewId) {
        Review review = reviewService.findById(reviewId).orElseThrow();

        RsData canDeleteRsData = reviewService.canDelete(rq.getMember(), review);

        if(canDeleteRsData.isFail()) return rq.historyBack(canDeleteRsData.getMsg());

        reviewService.delete(review);

        return "redirect:/review/check";
    }
}
