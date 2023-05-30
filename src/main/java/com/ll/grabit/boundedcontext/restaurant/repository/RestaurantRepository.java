package com.ll.grabit.boundedcontext.restaurant.repository;

import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
