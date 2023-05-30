package com.ll.grabit.boundedcontext.restaurant.entity;



import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantUpdateDto;
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

    private String description;

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
    public Restaurant(String restaurantName, String description, RestaurantType type, Address address, String detail_address, LocalTime openingTime, LocalTime closingTime, Integer perTimeMaxReservationCount) {
        this.restaurantName = restaurantName;
        this.description = description;
        this.type = type;
        this.address = address;
        this.detail_address = detail_address;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.perTimeMaxReservationCount = perTimeMaxReservationCount;
    }

    public RestaurantUpdateDto toRestaurantUpdateDto(){
        RestaurantUpdateDto restaurantUpdateDto = new RestaurantUpdateDto();
        restaurantUpdateDto.setRestaurantName(restaurantName);
        restaurantUpdateDto.setDescription(description);
        restaurantUpdateDto.setAddress1(address.getAddress1());
        restaurantUpdateDto.setAddress2(address.getAddress2());
        restaurantUpdateDto.setAddress3(address.getAddress3());
        restaurantUpdateDto.setDetail_address(detail_address);
        restaurantUpdateDto.setType(type.name());
        restaurantUpdateDto.setStartTime(openingTime.toString());
        restaurantUpdateDto.setEndTime(closingTime.toString());
        restaurantUpdateDto.setPerTimeMaxReservationCount(perTimeMaxReservationCount);

        return restaurantUpdateDto;
    }

    public void update(RestaurantUpdateDto restaurantUpdateDto, Address address, LocalTime startTime, LocalTime endTime) {
        restaurantName = restaurantUpdateDto.getRestaurantName();
        description = restaurantUpdateDto.getDescription();
        this.address = address;
        detail_address = restaurantUpdateDto.getDetail_address();
        type = RestaurantType.valueOf(restaurantUpdateDto.getType());
        openingTime = startTime;
        closingTime = endTime;
        perTimeMaxReservationCount = restaurantUpdateDto.getPerTimeMaxReservationCount();
    }
}
