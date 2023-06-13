package com.ll.grabit.boundedcontext.review.service;

import com.ll.grabit.base.rsdata.RsData;
import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.member.service.MemberService;
import com.ll.grabit.boundedcontext.reservation.dto.ReservationResponseDto;
import com.ll.grabit.boundedcontext.reservation.entity.Reservation;
import com.ll.grabit.boundedcontext.reservation.service.ReservationService;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.service.RestaurantService;
import com.ll.grabit.boundedcontext.review.entity.Review;
import com.ll.grabit.boundedcontext.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantService restaurantService;
    private final MemberService memberService;
    private final ReservationService reservationService;

    /*
    TODO
        1. 예약자인지 체크해야 함
        2. 예약상태가 방문완료인 사람인지 체크해야 함
        3. 이미 리뷰를 등록한 사람인지 체크해야 함
     */
    @Transactional
    public RsData<Review> addReview(String content, int rating, Long reservationId, Long memberId) {
        Member member = memberService.findByIdElseThrow(memberId);

        Reservation reservation = reservationService.findByIdElseThrow(reservationId);

        if (!reservation.getMember().getId().equals(member.getId())) {
            return RsData.of("F-1", "리뷰작성할 권한이 없습니다.");
        }

        if (!reservation.getStatus().equals("COMPLETED")) {
            return RsData.of("F-1", "방문완료를 한 후에 리뷰작성이 가능합니다.");
        }

        Optional<Review> opReview = reviewRepository.findByReservationReservationId(reservation.getReservationId());

        if (opReview.isPresent()) {
            return RsData.of("F-3", "이미 리뷰를 작성하셨습니다.");
        }

        Review review = createAndSave(content, rating, reservation.getRestaurant().getRestaurantId(), reservation.getReservationId(), member.getId());

        return RsData.of("S-1", "리뷰가 생성되었습니다.", review);
    }

    @Transactional
    public Review createAndSave(String content, int rating, Long restaurantId, Long reservationId, Long reviewerId) {
        Restaurant restaurant = restaurantService.findOne(restaurantId);
        Member reviewer = memberService.findByIdElseThrow(reviewerId);
        Reservation reservation = reservationService.findByIdElseThrow(reservationId);

        Review review = Review.builder()
                .content(content)
                .rating(rating)
                .reservation(reservation)
                .restaurant(restaurant)
                .reviewer(reviewer)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return reviewRepository.save(review);
    }

    public List<Review> findByReviewerId(Long id) {
        return reviewRepository.findByReviewerId(id);
    }

    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    public RsData canEdit(Member member, Review review) {
        if(member == null){
            return RsData.of("F-1", "먼저 로그인을 해주세요.");
        }

        if(member.getId() != review.getReviewer().getId()){
            return RsData.of("F-2", "리뷰를 수정할 권한이 없습니다.");
        }

        return RsData.of("S-1", "리뷰수정 가능합니다.");
    }

    @Transactional
    public RsData edit(Long id, Review updatedReview) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("리뷰를 찾을 수 없습니다. " + id));

        review.setContent(updatedReview.getContent());
        review.setRating(updatedReview.getRating());

        reviewRepository.save(review);

        return RsData.of("S-1", "리뷰를 수정하였습니다.");
    }

    public RsData canDelete(Member member, Review review) {
        if (review == null)
            return RsData.of("F-1", "이미 삭제되었습니다.");

        if(member.getId() != review.getReviewer().getId())
            return RsData.of("F-2", "삭제할 권한이 없습니다.");

        return RsData.of("S-1", "삭제가 가능합니다.");
    }

    @Transactional
    public RsData delete(Review review) {
        reviewRepository.delete(review);

        return RsData.of("S-1", "리뷰를 삭제하였습니다.");
    }
}
