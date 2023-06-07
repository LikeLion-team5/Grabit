package com.ll.grabit.boundedcontext.review.service;

import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.member.service.MemberService;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.service.RestaurantService;
import com.ll.grabit.boundedcontext.review.entity.Review;
import com.ll.grabit.boundedcontext.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantService restaurantService;
    private final MemberService memberService;

    /*
    TODO
        1. 예약자인지 체크해야 함
        2. 예약상태가 방문완료인 사람인지 체크해야 함
        3. 이미 리뷰를 등록한 사람인지 체크해야 함
     */
    public Review createAndSave(String content, int rating, Long restaurantId, Long reviewerId) {

        Restaurant restaurant = restaurantService.findOne(restaurantId);
        Member reviewer = memberService.findById(reviewerId);

        Review review = Review.builder()
                .content(content)
                .rating(rating)
                .restaurant(restaurant)
                .reviewer(reviewer)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);

        return savedReview;
    }
}