package com.ll.grabit.boundedcontext.review.repository;

import com.ll.grabit.boundedcontext.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReviewerId(Long reviewerId);

    Optional<Review> findByReservationReservationId(Long reservationId);

    List<Review> findByRestaurantRestaurantId(Long restaurantId);

    boolean existsByReservationReservationId(Long reservationId);
}
