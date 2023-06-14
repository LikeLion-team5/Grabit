package com.ll.grabit.boundedcontext.restaurant.dto;

import com.ll.grabit.boundedcontext.address.entity.Address;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.entity.RestaurantType;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.time.LocalTime;

@Getter
@Setter
@Data
public class RestaurantRegisterDto {

    @NotBlank(message = "식당 이름을 입력해주세요.")
    private String restaurantName;

    @NotBlank(message = "식당 소개를 입력해주세요.")
    private String description;
    @NotBlank(message = "식당 유형을 선택해주세요.")
    private String type;

    @Pattern(regexp = "^\\(?(\\d{2,3})\\)?[- ]?(\\d{3,4})[- ]?(\\d{4})$",
            message = "전화번호 형식이 잘못되었습니다. (올바른 형식: 123-4567-7890 or 02-990-1234)")
    @NotBlank(message = "전화번호를 입력해 주세요.")
    String phoneNumber;

    @NotBlank(message = "대주소를 입력해주세요.")
    private String address1;
    @NotBlank(message = "중주소를 입력해주세요.")
    private String address2;
//    @NotNull(message = "소주소를 입력해주세요.")
    private String address3;
    @NotBlank(message = "세부주소를 적어주세요.")
    private String detail_address;

    @NotBlank(message = "영업시작 시간을 입력해주세요.")
    private String startTime;

    @NotBlank(message = "영업마감 시간을 입력해주세요.")
    private String endTime;




    private String menuRegisterDtoList;


    public Restaurant toEntity(Address address, LocalTime startTime, LocalTime endTime) {
        return Restaurant.builder()
                .restaurantName(restaurantName)
                .description(description)
                .phoneNumber(phoneNumber)
                .address(address)
                .detail_address(detail_address)
                .openingTime(startTime)
                .closingTime(endTime)
                .type(RestaurantType.valueOf(type))
                .build();
    }
}
