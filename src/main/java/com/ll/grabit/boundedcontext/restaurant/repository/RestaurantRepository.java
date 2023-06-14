package com.ll.grabit.boundedcontext.restaurant.repository;

import com.ll.grabit.boundedcontext.address.entity.Address;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Page<Restaurant> findByAddressIn(List<Address> addressList, Pageable pageable);

    List<Review> findByRestaurantId(Long id);
}
