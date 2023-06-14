package com.ll.grabit.boundedcontext.review.service;

import com.ll.grabit.base.rsdata.RsData;
import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.member.service.MemberService;
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
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberService memberService;

    /*
    TODO
        1. 예약자인지 체크해야 함
        2. 예약상태가 방문완료인 사람인지 체크해야 함
        3. 이미 리뷰를 등록한 사람인지 체크해야 함
     */
    @Transactional
    public RsData<Review> addReview(String content, int rating, Reservation reservation, Long memberId) {
        Member member = memberService.findByIdElseThrow(memberId);

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

        RsData<Review> review = createAndSave(content, rating, reservation.getRestaurant(), reservation, member.getId());

        return RsData.of("S-1", "리뷰가 생성되었습니다.", review.getData());
    }

    @Transactional
    public RsData<Review> createAndSave(String content, int rating, Restaurant restaurant, Reservation reservation, Long reviewerId) {
        Member reviewer = memberService.findByIdElseThrow(reviewerId);

        Review review = Review.builder()
                .content(content)
                .rating(rating)
                .reservation(reservation)
                .restaurant(restaurant)
                .reviewer(reviewer)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review);

        // 식당에게 리뷰가 등록되었다고 알림
        restaurant.addReview(review);
        reservation.setReview(review);

        return RsData.of("S-1", "리뷰가 등록되었습니다.");
    }

    public List<Review> findByReviewerId(Long id) {
        return reviewRepository.findByReviewerId(id);
    }

    public RsData<List<Review>> getReviews(Long id, int sortCode){

        //로그인 했는지 확인
        if (id != null) {
            List<Review> reviews = findByReviewerId(id);

            Stream<Review> stream = reviews.stream();

            switch (sortCode) {
                case 2:
                    stream = stream.sorted(Comparator.comparing(Review::getId));
                    break;
                case 3:
                    stream = stream.sorted(Comparator.comparing(Review::getRating).reversed());
                    break;
                case 4:
                    stream = stream.sorted(Comparator.comparing(Review::getRating));
                    break;
                default:
                    stream = stream.sorted(Comparator.comparing(Review::getId).reversed());
                    break;

            }
            List<Review> newData = stream.collect(Collectors.toList());

            return RsData.of("S-1", "내가 작성한 리뷰들이 정렬되어 출력됩니다.", newData);
        }

        return RsData.of("F-1", "먼저 로그인부터 진행해주세요.");
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

    public List<Review> findReviews(Long restaurantId){
        return reviewRepository.findByRestaurantRestaurantId(restaurantId);
    }

    public double calculateAverageRating(Long restaurantId) {
        List<Review> reviews = findReviews(restaurantId);

        double sum = 0.0;
        for (Review review : reviews) {
            sum += review.getRating();
        }

        if(reviews.size() == 0)
            return 0;

        return sum / reviews.size();
    }

    public int countReviews(Long restaurantId) {
        List<Review> reviews = findReviews(restaurantId);
        return reviews.size();
    }
}