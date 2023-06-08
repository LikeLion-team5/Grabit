package com.ll.grabit.boundedcontext.review.repository;

import com.ll.grabit.boundedcontext.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
