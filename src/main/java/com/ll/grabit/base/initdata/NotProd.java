package com.ll.grabit.base.initdata;


import com.ll.grabit.boundedcontext.member.service.MemberService;
import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantRegisterDto;
import com.ll.grabit.boundedcontext.restaurant.service.RestaurantService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev","test"})
public class NotProd {

    @Bean
    public CommandLineRunner initData(
            MemberService memberService,
            RestaurantService restaurantService
    ) {
        return args -> {
            memberService.join("user1","1234");
            memberService.join("user2","1234");
            memberService.join("user3","1234");

            RestaurantRegisterDto restaurantDto = new RestaurantRegisterDto();
            restaurantDto.setRestaurantName("식당 이름");
            restaurantDto.setDescription("식당 소개");
            restaurantDto.setType("Korean");
            restaurantDto.setAddress1("서울특별시");
            restaurantDto.setAddress2("성북구");
            restaurantDto.setAddress3("돈암동");
            restaurantDto.setDetail_address("고려대학교 앞 건물 1층");
            restaurantDto.setStartTime("09:30");
            restaurantDto.setEndTime("23:30");
            restaurantDto.setPerTimeMaxReservationCount(3);
            for(int i=0; i<20; i++) {
                restaurantService.save(restaurantDto);
            }
        };
    }
}
