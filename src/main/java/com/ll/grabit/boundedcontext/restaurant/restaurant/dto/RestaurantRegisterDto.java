package com.ll.grabit.boundedcontext.restaurant.restaurant.dto;

import com.ll.grabit.boundedcontext.restaurant.restaurant.entity.Address;
import com.ll.grabit.boundedcontext.restaurant.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.restaurant.entity.RestaurantType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class RestaurantRegisterDto {

    @NotBlank(message = "식당 이름을 입력해주세요.")
    private String restaurantName;
    @NotBlank(message = "식당 유형을 선택해주세요.")
    private String type;


    private String address1;
    private String address2;
    private String address3;
    private String detail_address;

    private String startTime;

    private String endTime;

    private Integer perTimeMaxReservationCount;

    public Restaurant toEntity(Address address, LocalTime startTime, LocalTime endTime) {
        return Restaurant.builder()
                .restaurantName(restaurantName)
                .address(address)
                .detail_address(detail_address)
                .openingTime(startTime)
                .closingTime(endTime)
                .type(RestaurantType.valueOf(type))
                .perTimeMaxReservationCount(perTimeMaxReservationCount)
                .build();
    }
}
