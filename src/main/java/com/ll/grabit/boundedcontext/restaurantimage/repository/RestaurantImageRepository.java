package com.ll.grabit.boundedcontext.restaurantimage.repository;

import com.ll.grabit.boundedcontext.restaurantimage.entity.RestaurantImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantImageRepository extends JpaRepository<RestaurantImage, Long> {
}
