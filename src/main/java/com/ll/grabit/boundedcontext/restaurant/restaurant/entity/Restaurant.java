package com.ll.grabit.boundedcontext.restaurant.restaurant.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@NoArgsConstructor
@Getter
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    private String restaurantName;

    @Enumerated(EnumType.STRING)
    private RestaurantType type;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    private String detail_address;

    private LocalTime openingTime;
    private LocalTime closingTime;

    private Integer perTimeMaxReservationCount;

    @Builder
    public Restaurant(String restaurantName, RestaurantType type, Address address, String detail_address, LocalTime openingTime, LocalTime closingTime, Integer perTimeMaxReservationCount) {
        this.restaurantName = restaurantName;
        this.type = type;
        this.address = address;
        this.detail_address = detail_address;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.perTimeMaxReservationCount = perTimeMaxReservationCount;
    }
}
