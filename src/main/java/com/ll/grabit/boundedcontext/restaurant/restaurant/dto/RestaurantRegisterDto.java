package com.ll.grabit.boundedcontext.restaurant.restaurant.dto;

import com.ll.grabit.boundedcontext.restaurant.restaurant.entity.Address;
import com.ll.grabit.boundedcontext.restaurant.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.restaurant.entity.RestaurantType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.time.LocalTime;

@Getter
@Setter
public class RestaurantRegisterDto {

    @NotBlank(message = "식당 이름을 입력해주세요.")
    private String restaurantName;

    @NotBlank(message = "식당 소개를 입력해주세요.")
    private String description;
    @NotBlank(message = "식당 유형을 선택해주세요.")
    private String type;


    @NotBlank(message = "대주소를 입력해주세요.")
    private String address1;
    @NotBlank(message = "중주소를 입력해주세요.")
    private String address2;
    @NotBlank(message = "소주소를 입력해주세요.")
    private String address3;
    @NotBlank(message = "세부주소를 적어주세요.")
    private String detail_address;

    @NotBlank(message = "영업시작 시간을 입력해주세요.")
    private String startTime;

    @NotBlank(message = "영업마감 시간을 입력해주세요.")
    private String endTime;

    @Range(min = 1L, max = 5L, message = "1~5 사이의 숫자를 입력해주세요.")
    private Integer perTimeMaxReservationCount;

    public Restaurant toEntity(Address address, LocalTime startTime, LocalTime endTime) {
        return Restaurant.builder()
                .restaurantName(restaurantName)
                .description(description)
                .address(address)
                .detail_address(detail_address)
                .openingTime(startTime)
                .closingTime(endTime)
                .type(RestaurantType.valueOf(type))
                .perTimeMaxReservationCount(perTimeMaxReservationCount)
                .build();
    }
}
